import org.apache.spark.broadcast.Broadcast
import scala.io.Source
import scala.collection.mutable.Map
import scala.util.parsing.json.JSONObject
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import spark.implicits._

// Paths
val emolexPath = "/home/projgroup7/Desktop/NRC-Emotion-Lexicon-Wordlevel-v0.92.txt"
val reviewsPath = "/home/projgroup7/Desktop/Beauty_and_Personal_Care.jsonl"
val outputPath = "/home/projgroup7/Desktop/emotion_analysis_output"
val singleFileOutputPath = "/home/projgroup7/Desktop/emotion_analysis_output_single"

// Load and Prepare EmoLex Dictionary
def loadEmoLex(filepath: String): Map[String, List[String]] = {
val emotionDict = Map[String, List[String]](https://www.notion.so/Ayan-Jamal-14cce99cd0ca8065bed3f026c98cd325?pvs=21)
val fileSource = Source.fromFile(filepath)
for (line <- fileSource.getLines()) {
val parts = line.split("\t")
if (parts.length == 3) {
val word = parts(0)
val emotion = parts(1)
val value = parts(2).toInt
if (value == 1) {
emotionDict(word) = emotionDict.getOrElse(word, List()) :+ emotion
}
}
}
fileSource.close()
emotionDict
}

val emolex = loadEmoLex(emolexPath)
val emolexBroadcast: Broadcast[Map[String, List[String]]] = sc.broadcast(emolex)

// Define the Emotion Analysis Function
def analyzeEmotions(reviewText: String): Map[String, Int] = {
val words = reviewText.toLowerCase.split("\\s+")
val emotions = Map[String, Int](https://www.notion.so/Ayan-Jamal-14cce99cd0ca8065bed3f026c98cd325?pvs=21)
for (word <- words) {
if (emolexBroadcast.value.contains(word)) {
for (emotion <- emolexBroadcast.value(word)) {
emotions(emotion) = emotions.getOrElse(emotion, 0) + 1
}
}
}
emotions
}

// Load and process reviews (full dataset)
val reviewsDf = spark.read.json(reviewsPath)

// Filter out rows where text is null or empty before analysis
val requiredColsDf = reviewsDf
.select("asin", "user_id", "timestamp", "text")
.filter($"text".isNotNull && length(trim($"text")) > 0)

// Perform Emotion Analysis and Filtering at the RDD level
val processedRdd = requiredColsDf.rdd.flatMap { row =>
val asin = row.getAs[String](https://www.notion.so/%22asin%22)
val userId = row.getAs[String](https://www.notion.so/%22user_id%22)
val timestamp = row.getAs[Long](https://www.notion.so/%22timestamp%22)
val text = row.getAs[String](https://www.notion.so/%22text%22)

val emotionsMap = analyzeEmotions(text)

// Filter out empty or null emotions: if no emotions found, skip
if (emotionsMap.isEmpty) {
None
} else {
// Check positive and negative for neutral scenario
val pos = emotionsMap.getOrElse("positive", 0)
val neg = emotionsMap.getOrElse("negative", 0)


if (pos == neg && pos > 0) {
  // Replace positive and negative with neutral
  emotionsMap -= "positive"
  emotionsMap -= "negative"
  emotionsMap += ("neutral" -> pos)
}

// Convert to JSON
val emotionsJson = JSONObject(emotionsMap.toMap).toString()

Some((asin, userId, timestamp, emotionsJson))



}
}

val finalDf = processedRdd.toDF("asin", "user_id", "timestamp", "emotions")

// Write the result as JSON (multiple part files)
finalDf.write.mode("overwrite").json(outputPath)

// Now, merge the multiple part files into a single file by reading them back
val reReadDf = spark.read.json(outputPath)
reReadDf.coalesce(1).write.mode("overwrite").json(singleFileOutputPath)