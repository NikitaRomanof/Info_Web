package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.CheckMapper;
import edu.school21.info.model.dto.CheckDto;
import edu.school21.info.model.entity.Check;
import edu.school21.info.service.CheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@Slf4j
@RequestMapping("/checks")
public class CheckController {

    private final CheckService service;
    private final CheckMapper mapper;

    public CheckController(CheckService service, CheckMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<Check> checkEntity = service.listAll();
        List<CheckDto> checksDtos = mapper.toDtos(checkEntity);
        model.addAttribute("checks", checksDtos);
        return "checks/viewAllChecks";
    }

    @GetMapping("/add")
    public String create() {
        return "checks/addCheck";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("checkForm") CheckDto checkDto) {
        service.create(mapper.toEntity(checkDto));
        return "redirect:/checks";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/checks";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<Check> checkEntityOptional = service.getOne(id);
        checkEntityOptional.ifPresent(e -> model.addAttribute("check", mapper.toDto(e)));
        return "checks/updateCheck";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("checkForm") CheckDto checkDto) {
        service.update(mapper.toEntity(checkDto));
        return "redirect:/checks";
    }

}
