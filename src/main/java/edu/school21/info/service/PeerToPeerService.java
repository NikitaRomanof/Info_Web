package edu.school21.info.service;

import edu.school21.info.model.entity.PeerToPeer;
import edu.school21.info.repository.PeerToPeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeerToPeerService extends GenericService<PeerToPeer, Long> {

    private final PeerToPeerRepository peerToPeerRepository;

    @Autowired
    public PeerToPeerService(PeerToPeerRepository p2pRepo) {
        super(p2pRepo);
        this.peerToPeerRepository = p2pRepo;
    }
}
