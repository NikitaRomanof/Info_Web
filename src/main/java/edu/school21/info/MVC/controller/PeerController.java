package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.PeerMapper;
import edu.school21.info.model.dto.PeerDto;
import edu.school21.info.model.entity.Peer;
import edu.school21.info.service.PeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/peer")
public class PeerController {

    private final PeerService service;
    private final PeerMapper mapper;

    public PeerController(PeerService service, PeerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<Peer> peerEntity = service.listAll();
        List<PeerDto> peerDtos = mapper.toDtos(peerEntity);
        model.addAttribute("peer", peerDtos);
        return "peer/viewAllPeer";
    }

    @GetMapping("/add")
    public String create() {
        return "peer/addPeer";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("peerForm") PeerDto peerDto) {
        service.create(mapper.toEntity(peerDto));
        return "redirect:/peer";
    }

    @GetMapping("/delete/{nickname}")
    public String delete(@PathVariable String nickname) {
        service.delete(nickname);
        return "redirect:/peer";
    }

    @GetMapping("/update/{nickname}")
    public String update(@PathVariable String nickname, Model model) {
        Optional<Peer> peerEntityOptional = service.getOne(nickname);
        peerEntityOptional.ifPresent(e -> model.addAttribute("peer", mapper.toDto(e)));
        return "peer/updatePeer";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("peerForm") PeerDto peerDto) {
        service.update(mapper.toEntity(peerDto));
        return "redirect:/peer";
    }

}
