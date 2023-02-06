package edu.school21.info.mapper;

import edu.school21.info.model.dto.TimeTrackDto;
import edu.school21.info.model.entity.Peer;
import edu.school21.info.model.entity.TimeTrack;
import edu.school21.info.service.PeerService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TimeTrackMapper extends GenericMapper<TimeTrack, TimeTrackDto> {

    private final PeerService peerService;

    public TimeTrackMapper(PeerService peerService) {
        super(TimeTrack.class, TimeTrackDto.class);
        this.peerService = peerService;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(TimeTrack.class, TimeTrackDto.class)
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(TimeTrackDto.class, TimeTrack.class)
                .setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(TimeTrackDto source, TimeTrack destination)
            throws EntityNotFoundException, IllegalArgumentException {
        String nickname = source.getPeer();
        Peer peer = peerService.getByNicknameOrElseThrow(nickname);
        destination.setPeer(peer);
    }

    @Override
    void mapSpecificFields(TimeTrack source, TimeTrackDto destination) {
        destination.setPeer(source.getPeer().getNickname());
    }

}
