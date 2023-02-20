package edu.school21.info.service;

import edu.school21.info.model.entity.Check;
import edu.school21.info.repository.CheckRepository;
import edu.school21.info.repository.GenericRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckService extends GenericService<Check, Long> {

    private final CheckRepository checkRepository;

    @Autowired
    public CheckService(CheckRepository repository) {
        super(repository);
        this.checkRepository = repository;
    }

    public Check getByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Check with id '%d' not found", id)));
    }

    public List<Long> getAllIds() {
        return checkRepository.findAllIdsAsc();
    }

}
