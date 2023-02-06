package edu.school21.info.service;

import edu.school21.info.model.entity.Experience;
import edu.school21.info.repository.ExperienceRepository;
import edu.school21.info.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExperienceService extends GenericService<Experience, Long> {

    private final ExperienceRepository experienceRepository;

    @Autowired
    public ExperienceService(ExperienceRepository experienceRepository) {
        super(experienceRepository);
        this.experienceRepository = experienceRepository;
    }
}
