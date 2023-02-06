package edu.school21.info.service;

import edu.school21.info.model.entity.Peer;
import edu.school21.info.repository.GenericRepository;
import edu.school21.info.repository.PeerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PeerService extends GenericService<Peer, String> {

    private final PeerRepository peerRepository;

    @Autowired
    protected PeerService(PeerRepository repository) {
        super(repository);
        peerRepository = repository;
    }

    public Optional<Peer> getByNickname(String nickname) {
        return peerRepository.findPeerByNickname(nickname);
    }

    public Peer getByNicknameOrElseThrow(String nickname) {
        return peerRepository.findPeerByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Peer with nickname '%s' not found", nickname)));
    }
}
