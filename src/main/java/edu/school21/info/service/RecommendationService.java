package edu.school21.info.service;

import edu.school21.info.model.entity.Recommendation;
import edu.school21.info.repository.GenericRepository;
import edu.school21.info.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService extends GenericService<Recommendation, Long> {

    private final RecommendationRepository recommendationRepository;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository) {
        super(recommendationRepository);
        this.recommendationRepository = recommendationRepository;
    }
}
