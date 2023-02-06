package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.ExperienceMapper;
import edu.school21.info.model.dto.ExperienceDto;
import edu.school21.info.model.entity.Experience;
import edu.school21.info.service.ExperienceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceService service;
    private final ExperienceMapper mapper;

    public ExperienceController(ExperienceService service, ExperienceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<Experience> expEntity = service.listAll();
        List<ExperienceDto> experienceDtos = mapper.toDtos(expEntity);
        model.addAttribute("experience", experienceDtos);
        return "experience/viewAllExperience";
    }

    @GetMapping("/add")
    public String create() {
        return "experience/addExperience";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("experienceForm") ExperienceDto experienceDto) {
        service.create(mapper.toEntity(experienceDto));
        return "redirect:/experience";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/experience";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<Experience> experienceEntityOptional = service.getOne(id);
        experienceEntityOptional.ifPresent(e -> model.addAttribute("exp", mapper.toDto(e)));
        return "experience/updateExperience";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("experienceForm") ExperienceDto experienceDto) {
        service.update(mapper.toEntity(experienceDto));
        return "redirect:/experience";
    }
}
