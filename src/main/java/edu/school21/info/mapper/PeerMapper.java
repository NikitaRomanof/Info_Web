package edu.school21.info.mapper;

import edu.school21.info.model.dto.PeerDto;
import edu.school21.info.model.entity.Peer;
import org.springframework.stereotype.Component;

@Component
public class PeerMapper extends GenericMapper<Peer, PeerDto> {

    public PeerMapper() {
        super(Peer.class, PeerDto.class);
    }

}
