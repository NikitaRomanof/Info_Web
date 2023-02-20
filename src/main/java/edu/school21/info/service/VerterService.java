package edu.school21.info.service;

import edu.school21.info.model.entity.Verter;
import edu.school21.info.repository.GenericRepository;
import edu.school21.info.repository.VerterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerterService extends GenericService<Verter, Long> {

    private final VerterRepository verterRepository;

    @Autowired
    public VerterService(VerterRepository verterRepository) {
        super(verterRepository);
        this.verterRepository = verterRepository;
    }
}
