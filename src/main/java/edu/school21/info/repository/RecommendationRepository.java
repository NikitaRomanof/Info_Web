package edu.school21.info.repository;

import edu.school21.info.model.entity.Recommendation;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends GenericRepository<Recommendation, Long> {
}
