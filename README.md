# Spark Sentiment and Emotion Analysis

**Repository:** [github.com/Kenterum/spark-sentiment-and-emotion-analysis](https://github.com/Kenterum/spark-sentiment-and-emotion-analysis)

This project performs **sentiment and emotion analysis** on Amazon reviews for the _Beauty & Personal Care_ category, leveraging **Apache Spark** for distributed computing and **NRC EmoLex** for emotion detection. By combining rule-based sentiment scoring and lexicon-based emotion mapping, each review is enriched with both **sentiment** (positive, negative) and **emotions** (e.g., _anger_, _joy_, _trust_). We provide a **console application** and a **Java/Spring Boot API** to query and display the top reviews by a chosen emotion or sentiment level.

## Table of Contents
1. [Project Workflow & Outcomes](#project-workflow--outcomes)
2. [Folder Structure](#folder-structure)
3. [How to Use](#how-to-use)
4. [Problems Encountered](#problems-encountered)

---

## Project Workflow & Outcomes

1. **Spark Emotion & Sentiment Analysis (Scala):**
   - Loads Amazon reviews in JSON format.
   - Filters out empty or null texts.
   - Tokenizes and maps each word to **NRC EmoLex** for **emotion counts**.
   - Applies basic **sentiment logic** (e.g., positive vs. negative words).
   - Merges results into a final JSON dataset with top-level integer fields for each emotion plus a sentiment score (where relevant).

2. **Optional Python Transform & Console:**
   - **`transform_emotions.py`:** Further restructure or clean the final data if needed.
   - **`menu_app.py`:** Lets you pick an emotion (e.g., “anger”) or sentiment threshold (“top negative” reviews) and see the top reviews quickly.

3. **Spring Boot API (Java):**
   - Endpoints like `/api/top/{emotion}?limit=N` or `/api/topSentiment?limit=N` can return top reviews sorted by the chosen field.
   - `/api/review/{asin}/{user_id}/{timestamp}` fetches original text and associated emotions/sentiment score.

### Final Outcome
- A JSON-based output linking each review with:
  - **Emotion fields** (`anger`, `joy`, `trust`, etc.).
  - **Sentiment** (positive, negative) assigned by rule-based or lexical matching.
- These results enable more nuanced filtering, recommendations, and queries—either via the console or REST endpoints.

---

## Folder Structure

```
spark-sentiment-and-emotion-analysis/
├── spark-code/
│   ├── src/
│   │   └── main/
│   │       └── scala/
│   │           └── EmotionAnalysis.scala
│   ├── pom.xml (or build.sbt)
│   └── README.md
├── python-console/
│   └── console-app.py
├── server-api/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/amazonreviewapi/
│   │       │       ├── controller/
│   │       │       │   └── AmazonController.java
│   │       │       ├── model/
│   │       │       │   └── ProcessedEmotion.java, Review.java
│   │       │       └── repo/
│   │       │           └── (ProcessedEmotionRepository.java, ReviewRepository.java)
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
└── README.md (This file)
```

**Key Points:**
- **`spark-code/`**: Scala Spark job to read, filter, and produce sentiment/emotion-enriched reviews.
- **`python-console/`**: Python scripts for data transformation and an interactive console to query top reviews by emotion/sentiment.
- **`server-api/`**: Java/Spring Boot application providing REST endpoints for top reviews and full-text retrieval.

---

## How to Use

1. **Clone the Repository & Install Dependencies**
   ```bash
   git clone https://github.com/Kenterum/spark-sentiment-and-emotion-analysis.git
   cd spark-sentiment-and-emotion-analysis
   ```

2. **Run Spark Code (Scala)**
   - Go to `spark-code/`.
   - If using Maven:
     ```bash
     mvn clean install
     mvn exec:java -Dexec.mainClass="com.yourpackage.EmotionAnalysis"
     ```
   - Adjust input/output paths in `EmotionAnalysis.scala` or `pom.xml` to match your environment.

3. **Python Console Scripts (Optional)**
   - Move to `python-console/`.
   - Create a virtual environment (optional) and install:
     ```bash
     pip install -r requirements.txt
     ```
   - **`transform_emotions.py`:** Additional cleaning/resizing if you have large output from Spark.
   - **`menu_app.py`:**  
     ```bash
     python menu_app.py
     ```
     Enter an emotion like “anger” and a limit (e.g., 10) to see top results.  
     Or choose “top negative” if you integrated a negative sentiment filter.

4. **Spring Boot API**
   - In `server-api/amazonreviewapi/`, run:
     ```bash
     mvn clean install
     mvn spring-boot:run
     ```
   - Endpoints:
     - `GET /api/top/{emotion}?limit=N`
     - `GET /api/review/{asin}/{user_id}/{timestamp}`
   - By default, the server is at [http://localhost:8080](http://localhost:8080).

---

## Problems Encountered

1. **High-Scored Spam Comments**  
   - Some reviews were artificially long or repetitive, inflating counts for certain words and skewing sentiment/emotion results. We partially addressed this by filtering out extremely lengthy texts, but more advanced spam detection could further improve data quality.

2. **Irony / Sarcasm Challenges**  
   - Statements like “I absolutely loved how terrible this was” are difficult to classify via simple lexicons. Without deep contextual modeling, sarcasm can be misread as positive or negative incorrectly. This remains a future enhancement area (e.g., training ML models or implementing advanced language features).

3. **Balancing Lexicon-Based and Rule-Based Methods**  
   - Merging sentiment and emotion logic occasionally produced edge cases, such as `(positive == negative) → neutral`. Some reviews legitimately contain multiple contrasting words. Ensuring such logic doesn’t oversimplify complex user feedback was a key concern.

---
