package edu.school21.info.repository;

import edu.school21.info.model.entity.TransferredPoints;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferredPointsRepository extends GenericRepository<TransferredPoints, Long> {
}
