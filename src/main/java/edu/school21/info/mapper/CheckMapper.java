package edu.school21.info.mapper;

import edu.school21.info.model.dto.CheckDto;
import edu.school21.info.model.entity.Check;
import edu.school21.info.model.entity.Peer;
import edu.school21.info.model.entity.Task;
import edu.school21.info.service.PeerService;
import edu.school21.info.service.TaskService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CheckMapper extends GenericMapper<Check, CheckDto> {

    private final PeerService peerService;
    private final TaskService taskService;

    public CheckMapper(PeerService peerService, TaskService taskService) {
        super(Check.class, CheckDto.class);
        this.peerService = peerService;
        this.taskService = taskService;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Check.class, CheckDto.class).setPostConverter(toDtoConverter());
        mapper.createTypeMap(CheckDto.class, Check.class).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(CheckDto source, Check destination)
            throws EntityNotFoundException {

        String nickname = source.getPeer();
        Peer peer = peerService.getByNicknameOrElseThrow(nickname);
        destination.setPeer(peer);

        String title = source.getTask();
        Task task = taskService.getByTitleOrElseThrow(title);
        destination.setTask(task);

    }

    @Override
    void mapSpecificFields(Check source, CheckDto destination) {
        destination.setPeer(source.getPeer().getNickname());
        destination.setTask(source.getTask().getTitle());
    }

}
