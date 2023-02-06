package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.PeerToPeerMapper;
import edu.school21.info.model.dto.PeerToPeerDto;
import edu.school21.info.model.entity.PeerToPeer;
import edu.school21.info.service.PeerToPeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/peerToPeer")
public class PeerToPeerController {

    private final PeerToPeerService service;
    private final PeerToPeerMapper mapper;

    public PeerToPeerController(PeerToPeerService service, PeerToPeerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<PeerToPeer> peerToPeerEntity = service.listAll();
        List<PeerToPeerDto> peerToPeerDtos = mapper.toDtos(peerToPeerEntity);
        model.addAttribute("peerToPeer", peerToPeerDtos);
        return "peerToPeer/viewAllPeerToPeer";
    }


    @GetMapping("/add")
    public String create() {
        return "peerToPeer/addPeerToPeer";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("peerToPeerForm") PeerToPeerDto peerToPeerDto) {
        System.err.println(mapper.toEntity(peerToPeerDto));
        service.create(mapper.toEntity(peerToPeerDto));
        return "redirect:/peerToPeer";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/peerToPeer";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<PeerToPeer> peerToPeerEntityOptional = service.getOne(id);
        peerToPeerEntityOptional.ifPresent(e -> model.addAttribute("peerToPeer", mapper.toDto(e)));
        return "peerToPeer/updatePeerToPeer";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("peerToPeerForm") PeerToPeerDto peerToPeerDto) {
        service.update(mapper.toEntity(peerToPeerDto));
        return "redirect:/peerToPeer";
    }

}
