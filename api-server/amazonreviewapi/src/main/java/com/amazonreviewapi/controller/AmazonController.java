package com.amazonreviewapi.controller;

import com.amazonreviewapi.model.ProcessedEmotion;
import com.amazonreviewapi.repo.ReviewRepository;
import com.amazonreviewapi.repo.ProcessedEmotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

@RestController
@RequestMapping("/api")
public class AmazonController {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private ProcessedEmotionRepository emoRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/top/{emotion}")
    public List<Map<String,Object>> getTopByEmotion(
            @PathVariable("emotion") String emotion,
            @RequestParam(value="limit", defaultValue="20") int limit) {

        // Use aggregation to sort and limit on the server side
        Aggregation agg = Aggregation.newAggregation(
            sort(Sort.by(Sort.Direction.DESC, emotion)),
            limit(limit)
        );

        AggregationResults<ProcessedEmotion> results = mongoTemplate.aggregate(agg, "processedEmotions", ProcessedEmotion.class);
        List<ProcessedEmotion> topResults = results.getMappedResults();

        // Convert the results to the desired output format
        List<Map<String,Object>> response = new ArrayList<>();
        for (ProcessedEmotion pe : topResults) {
            Map<String,Object> map = new HashMap<>();
            map.put("asin", pe.getAsin());
            map.put("user_id", pe.getUserId());
            map.put("timestamp", pe.getTimestamp());
            map.put(emotion, getEmotionScore(pe, emotion));
            response.add(map);
        }

        return response;
    }

    // Helper method to dynamically get the score for a given emotion
    private int getEmotionScore(ProcessedEmotion pe, String emotion) {
        switch (emotion.toLowerCase()) {
            case "anger": return pe.getAnger();
            case "positive": return pe.getPositive();
            case "negative": return pe.getNegative();
            case "joy": return pe.getJoy();
            case "anticipation": return pe.getAnticipation();
            case "trust": return pe.getTrust();
            case "surprise": return pe.getSurprise();
            case "fear": return pe.getFear();
            case "disgust": return pe.getDisgust();
            case "sadness": return pe.getSadness();
            case "neutral": return pe.getNeutral();
            default: return 0;
        }
    }

    @GetMapping("/review/{asin}/{user_id}/{timestamp}")
    public Map<String, Object> getReviewText(
        @PathVariable("asin") String asin,
        @PathVariable("user_id") String userId,
        @PathVariable("timestamp") long timestamp) {

        var r = reviewRepo.findByAsinAndUserIdAndTimestamp(asin, userId, timestamp);
        Map<String,Object> response = new HashMap<>();
        if (r != null) {
            response.put("asin", r.getAsin());
            response.put("user_id", r.getUserId());
            response.put("timestamp", r.getTimestamp());
            response.put("text", r.getText());
        } else {
            response.put("error", "Review not found");
        }
        return response;
    }
}
