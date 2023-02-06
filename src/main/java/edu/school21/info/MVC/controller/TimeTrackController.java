package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.TimeTrackMapper;
import edu.school21.info.model.dto.TimeTrackDto;
import edu.school21.info.model.entity.TimeTrack;
import edu.school21.info.service.TimeTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/timeTrack")
public class TimeTrackController {

    private final TimeTrackService service;
    private final TimeTrackMapper mapper;

    public TimeTrackController(TimeTrackService service, TimeTrackMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<TimeTrack> timeTrackEntity = service.listAll();
        List<TimeTrackDto> timeTrackDtos = mapper.toDtos(timeTrackEntity);
        model.addAttribute("timeTrack", timeTrackDtos);
        return "timeTrack/viewAllTimeTrack";
    }

    @GetMapping("/add")
    public String create() {
        return "timeTrack/addTimeTrack";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("timeTrackForm") TimeTrackDto timeTrackDto) {
        service.create(mapper.toEntity(timeTrackDto));
        return "redirect:/timeTrack";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/timeTrack";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<TimeTrack> timeTrackEntityOptional = service.getOne(id);
        timeTrackEntityOptional.ifPresent(e -> model.addAttribute("timeTrack", mapper.toDto(e)));
        return "timeTrack/updateTimeTrack";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("timeTrackForm") TimeTrackDto timeTrackDto) {
        service.update(mapper.toEntity(timeTrackDto));
        return "redirect:/timeTrack";
    }
}
