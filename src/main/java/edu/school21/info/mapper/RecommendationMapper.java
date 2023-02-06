package edu.school21.info.mapper;

import edu.school21.info.model.dto.RecommendationDto;
import edu.school21.info.model.entity.Peer;
import edu.school21.info.model.entity.Recommendation;
import edu.school21.info.service.PeerService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RecommendationMapper extends GenericMapper<Recommendation, RecommendationDto> {

    private final PeerService peerService;

    public RecommendationMapper(PeerService peerService) {
        super(Recommendation.class, RecommendationDto.class);
        this.peerService = peerService;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Recommendation.class, RecommendationDto.class)
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(RecommendationDto.class, Recommendation.class)
                .setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(RecommendationDto source, Recommendation destination) throws EntityNotFoundException {

        Peer peer = peerService.getByNicknameOrElseThrow(source.getPeer());
        destination.setPeer(peer);

        String nickname = source.getRecommendedPeer();
        if (nickname != null && !nickname.isEmpty()) {
            Peer recommended = peerService.getByNicknameOrElseThrow(nickname);
            destination.setRecommendedPeer(recommended);
        }
    }

    @Override
    void mapSpecificFields(Recommendation source, RecommendationDto destination) {
        destination.setPeer(source.getPeer().getNickname());

        Peer recommendedPeer = source.getRecommendedPeer();
        if (recommendedPeer != null) {
            destination.setRecommendedPeer(recommendedPeer.getNickname());
        }
    }

}
