package com.amazonreviewapi.repo;

import com.amazonreviewapi.model.ProcessedEmotion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcessedEmotionRepository extends MongoRepository<ProcessedEmotion, String> {
    ProcessedEmotion findByAsinAndUserIdAndTimestamp(String asin, String userId, long timestamp);
}
