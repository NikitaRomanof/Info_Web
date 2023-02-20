package edu.school21.info.controller.data;

import edu.school21.info.controller.GenericController;
import edu.school21.info.mapper.PeerToPeerMapper;
import edu.school21.info.model.dto.PeerToPeerDto;
import edu.school21.info.model.entity.PeerToPeer;
import edu.school21.info.service.CheckService;
import edu.school21.info.service.PeerService;
import edu.school21.info.service.PeerToPeerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Getter
@RequestMapping("/peerToPeer")
public class PeerToPeerController extends GenericController<PeerToPeer, PeerToPeerDto, Long> {

    private final PeerToPeerService service;
    private final PeerToPeerMapper mapper;
    private final PeerService peerService;
    private final CheckService checkService;

    @Override
    protected Class<PeerToPeerDto> getDtoClass() {
        return PeerToPeerDto.class;
    }

    @Override
    protected String getEntityName() {
        return "peerToPeer";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("peers", peerService.getAllNicknames());
        model.addAttribute("checkIds", checkService.getAllIds());
        return super.update(id, model);
    }

    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("peers", peerService.getAllNicknames());
        model.addAttribute("checkIds", checkService.getAllIds());
        return super.create(model);
    }
}

