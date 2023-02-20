package edu.school21.info.controller.data;

import edu.school21.info.controller.GenericController;
import edu.school21.info.mapper.ExperienceMapper;
import edu.school21.info.model.dto.ExperienceDto;
import edu.school21.info.model.entity.Experience;
import edu.school21.info.service.CheckService;
import edu.school21.info.service.ExperienceService;
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
@RequestMapping("/experience")
public class ExperienceController extends GenericController<Experience, ExperienceDto, Long> {

    private final ExperienceService service;
    private final ExperienceMapper mapper;
    private final CheckService checkService;

    @Override
    protected Class<ExperienceDto> getDtoClass() {
        return ExperienceDto.class;
    }

    @Override
    protected String getEntityName() {
        return "experience";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("checkIds", checkService.getAllIds());
        return super.update(id, model);
    }

    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("checkIds", checkService.getAllIds());
        return super.create(model);
    }
}