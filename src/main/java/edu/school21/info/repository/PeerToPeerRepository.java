package edu.school21.info.repository;

import edu.school21.info.model.entity.PeerToPeer;
import org.springframework.stereotype.Repository;

@Repository
public interface PeerToPeerRepository extends GenericRepository<PeerToPeer, Long> {
}
