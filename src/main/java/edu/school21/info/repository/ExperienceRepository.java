package edu.school21.info.repository;

import edu.school21.info.model.entity.Experience;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends GenericRepository<Experience, Long> {
}
