package edu.school21.info.repository;

import edu.school21.info.model.entity.Peer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeerRepository extends GenericRepository<Peer, String> {
    Optional<Peer> findPeerByNickname(String nickname);
}
