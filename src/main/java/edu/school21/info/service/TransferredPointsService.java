package edu.school21.info.service;

import edu.school21.info.model.entity.TransferredPoints;
import edu.school21.info.repository.GenericRepository;
import edu.school21.info.repository.TransferredPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferredPointsService extends GenericService<TransferredPoints, Long> {

    private final TransferredPointsRepository transferredPointsRepository;

    @Autowired
    public TransferredPointsService(TransferredPointsRepository transferredPointsRepository) {
        super(transferredPointsRepository);
        this.transferredPointsRepository = transferredPointsRepository;
    }
}
