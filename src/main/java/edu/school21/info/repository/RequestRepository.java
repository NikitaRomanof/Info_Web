package edu.school21.info.repository;

import edu.school21.info.model.entity.CallBody;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends GenericRepository<CallBody, Long> {
}

