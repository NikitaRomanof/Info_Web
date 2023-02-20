package edu.school21.info.service;

import edu.school21.info.model.entity.TimeTrack;
import edu.school21.info.repository.GenericRepository;
import edu.school21.info.repository.TimeTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTrackService extends GenericService<TimeTrack, Long> {

    private final TimeTrackRepository timeTrackRepository;

    @Autowired
    public TimeTrackService(TimeTrackRepository timeTrackRepository) {
        super(timeTrackRepository);
        this.timeTrackRepository = timeTrackRepository;
    }
}
