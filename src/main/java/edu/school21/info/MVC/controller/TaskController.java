package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.TaskMapper;
import edu.school21.info.model.dto.TaskDto;
import edu.school21.info.model.entity.Task;
import edu.school21.info.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/task")
public class TaskController {

    private final TaskService service;
    private final TaskMapper mapper;

    public TaskController(TaskService service, TaskMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<Task> taskEntity = service.listAll();
        List<TaskDto> taskDtos = mapper.toDtos(taskEntity);
        model.addAttribute("task", taskDtos);
        return "task/viewAllTask";
    }

    @GetMapping("/add")
    public String create() {
        return "task/addTask";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("taskForm") TaskDto taskDto) {
        service.create(mapper.toEntity(taskDto));
        return "redirect:/task";
    }

    @GetMapping("/delete/{title}")
    public String delete(@PathVariable String title) {
        service.delete(title);
        return "redirect:/task";
    }

    @GetMapping("/update/{title}")
    public String update(@PathVariable String title, Model model) {
        Optional<Task> taskEntityOptional = service.getOne(title);
        taskEntityOptional.ifPresent(e -> model.addAttribute("task", mapper.toDto(e)));
        return "task/updateTask";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("taskForm") TaskDto taskDto) {
        service.update(mapper.toEntity(taskDto));
        return "redirect:/task";
    }
}
