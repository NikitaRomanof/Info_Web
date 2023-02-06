package edu.school21.info.repository;

import edu.school21.info.model.entity.TimeTrack;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTrackRepository extends GenericRepository<TimeTrack, Long> {
}
