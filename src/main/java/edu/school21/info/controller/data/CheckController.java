package edu.school21.info.controller.data;

import edu.school21.info.controller.GenericController;
import edu.school21.info.mapper.CheckMapper;
import edu.school21.info.mapper.PeerMapper;
import edu.school21.info.mapper.TaskMapper;
import edu.school21.info.model.dto.CheckDto;
import edu.school21.info.model.entity.Check;
import edu.school21.info.service.CheckService;
import edu.school21.info.service.PeerService;
import edu.school21.info.service.TaskService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@Getter
@RequestMapping("/checks")
@RequiredArgsConstructor
public class CheckController extends GenericController<Check, CheckDto, Long> {

    private final CheckService service;
    private final CheckMapper mapper;
    private final PeerService peerService;
    private final TaskService taskService;

    @Override
    protected Class<CheckDto> getDtoClass() {
        return CheckDto.class;
    }

    @Override
    protected String getEntityName() {
        return "checks";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("peers", peerService.getAllNicknames());
        model.addAttribute("tasks", taskService.getAllTitles());
        return super.update(id, model);
    }

    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("peers", peerService.getAllNicknames());
        model.addAttribute("tasks", taskService.getAllTitles());
        return super.create(model);
    }
}