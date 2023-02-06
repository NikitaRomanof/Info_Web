package edu.school21.info.mapper;

import edu.school21.info.model.dto.FriendsDto;
import edu.school21.info.model.entity.Friends;
import edu.school21.info.model.entity.Peer;
import edu.school21.info.service.PeerService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class FriendsMapper extends GenericMapper<Friends, FriendsDto> {

    private final PeerService peerService;

    public FriendsMapper(PeerService peerService) {
        super(Friends.class, FriendsDto.class);
        this.peerService = peerService;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Friends.class, FriendsDto.class).setPostConverter(toDtoConverter());
        mapper.createTypeMap(FriendsDto.class, Friends.class).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(FriendsDto source, Friends destination) throws EntityNotFoundException {
        Peer firstPeer = peerService.getByNicknameOrElseThrow(source.getFirstPeer());
        Peer secondPeer = peerService.getByNicknameOrElseThrow(source.getSecondPeer());
        destination.setFirstPeer(firstPeer);
        destination.setSecondPeer(secondPeer);
    }

    @Override
    void mapSpecificFields(Friends source, FriendsDto destination) {
        destination.setFirstPeer(source.getFirstPeer().getNickname());
        destination.setSecondPeer(source.getSecondPeer().getNickname());
    }

}
