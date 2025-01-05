package com.amazonreviewapi.repo;

import com.amazonreviewapi.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
Review findByAsinAndUserIdAndTimestamp(String asin, String userId, long timestamp);
}
