package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.VerterMapper;
import edu.school21.info.model.dto.VerterDto;
import edu.school21.info.model.entity.Verter;
import edu.school21.info.service.VerterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/verter")
public class VerterController {

    private final VerterService service;
    private final VerterMapper mapper;

    public VerterController(VerterService service, VerterMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<Verter> verterEntity = service.listAll();
        List<VerterDto> verterDtos = mapper.toDtos(verterEntity);
        model.addAttribute("verter", verterDtos);
        return "verter/viewAllVerter";
    }

    @GetMapping("/add")
    public String create() {
        return "verter/addVerter";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("verterForm") VerterDto verterDto) {
        service.create(mapper.toEntity(verterDto));
        return "redirect:/verter";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/verter";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<Verter> verterEntityOptional = service.getOne(id);
        verterEntityOptional.ifPresent(e -> model.addAttribute("verter", mapper.toDto(e)));
        return "verter/updateVerter";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("verterForm") VerterDto verterDto) {
        service.update(mapper.toEntity(verterDto));
        return "redirect:/verter";
    }
}
