package com.amazonreviewapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="processedEmotions")
public class ProcessedEmotion {

    @Id
    private String id;
    private String asin;

    @Field("user_id") // Maps user_id field in MongoDB to userId in Java
    private String userId;

    private long timestamp;

    // Now each emotion is an integer field
    private int anger;
    private int positive;
    private int negative;
    private int joy;
    private int anticipation;
    private int trust;
    private int surprise;
    private int fear;
    private int disgust;
    private int sadness;
    private int neutral;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAsin() { return asin; }
    public void setAsin(String asin) { this.asin = asin; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getAnger() { return anger; }
    public void setAnger(int anger) { this.anger = anger; }

    public int getPositive() { return positive; }
    public void setPositive(int positive) { this.positive = positive; }

    public int getNegative() { return negative; }
    public void setNegative(int negative) { this.negative = negative; }

    public int getJoy() { return joy; }
    public void setJoy(int joy) { this.joy = joy; }

    public int getAnticipation() { return anticipation; }
    public void setAnticipation(int anticipation) { this.anticipation = anticipation; }

    public int getTrust() { return trust; }
    public void setTrust(int trust) { this.trust = trust; }

    public int getSurprise() { return surprise; }
    public void setSurprise(int surprise) { this.surprise = surprise; }

    public int getFear() { return fear; }
    public void setFear(int fear) { this.fear = fear; }

    public int getDisgust() { return disgust; }
    public void setDisgust(int disgust) { this.disgust = disgust; }

    public int getSadness() { return sadness; }
    public void setSadness(int sadness) { this.sadness = sadness; }

    public int getNeutral() { return neutral; }
    public void setNeutral(int neutral) { this.neutral = neutral; }
}









