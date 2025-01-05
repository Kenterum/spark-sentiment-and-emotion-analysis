package com.amazonreviewapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;
import java.util.Map;

@Document(collection="reviews")
public class Review {
    @Id
    private String id;
    private String asin;

    @Field("user_id") // Maps the MongoDB field "user_id" to the Java field "userId".
    private String userId;

    private long timestamp;
    private String text;
    private int rating;
    private String title;

    @Field("verified_purchase") // In case the DB field is exactly "verified_purchase".
    private boolean verified_purchase;

    @Field("helpful_vote") // If the DB field is exactly "helpful_vote".
    private long helpful_vote;

    @Field("parent_asin") // If the DB field is exactly "parent_asin".
    private String parent_asin;

    private List<Map<String,Object>> images;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAsin() { return asin; }
    public void setAsin(String asin) { this.asin = asin; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isVerified_purchase() { return verified_purchase; }
    public void setVerified_purchase(boolean verified_purchase) { this.verified_purchase = verified_purchase; }

    public long getHelpful_vote() { return helpful_vote; }
    public void setHelpful_vote(long helpful_vote) { this.helpful_vote = helpful_vote; }

    public String getParent_asin() { return parent_asin; }
    public void setParent_asin(String parent_asin) { this.parent_asin = parent_asin; }

    public List<Map<String,Object>> getImages() { return images; }
    public void setImages(List<Map<String,Object>> images) { this.images = images; }
}
