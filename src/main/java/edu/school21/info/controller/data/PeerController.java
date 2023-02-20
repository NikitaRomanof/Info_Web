package edu.school21.info.controller.data;

import edu.school21.info.controller.GenericController;
import edu.school21.info.mapper.PeerMapper;
import edu.school21.info.model.dto.PeerDto;
import edu.school21.info.model.entity.Peer;
import edu.school21.info.service.PeerService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Getter
@RequestMapping("/peer")
public class PeerController extends GenericController<Peer, PeerDto, String> {

    private final PeerService service;
    private final PeerMapper mapper;

    @Override
    protected Class<PeerDto> getDtoClass() {
        return PeerDto.class;
    }

    @Override
    protected String getEntityName() {
        return "peer";
    }

}
