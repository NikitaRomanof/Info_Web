package edu.school21.info.service;

import edu.school21.info.model.entity.CallBody;
import edu.school21.info.repository.CallBodyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallBodyService extends GenericService<CallBody, Long> {

    private final CallBodyRepository repository;

    @Autowired
    public CallBodyService(CallBodyRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public CallBody getOneOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Procedure with id %d not found", id)));
    }
}