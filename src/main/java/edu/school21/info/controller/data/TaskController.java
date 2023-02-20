package edu.school21.info.controller.data;

import edu.school21.info.controller.GenericController;
import edu.school21.info.mapper.TaskMapper;
import edu.school21.info.model.dto.TaskDto;
import edu.school21.info.model.entity.Task;
import edu.school21.info.service.TaskService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Getter
@RequestMapping("/task")
public class TaskController extends GenericController<Task, TaskDto, String> {

    private final TaskService service;
    private final TaskMapper mapper;

    @Override
    protected Class<TaskDto> getDtoClass() {
        return TaskDto.class;
    }

    @Override
    protected String getEntityName() {
        return "task";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model) {
        Optional<Task> entity = service.getOne(id);
        if (entity.isPresent()) {
            Task task = entity.get();
            List<String> parents = task.isFirstTask() ?
                    Collections.singletonList("") :
                    service.getAllAvailableParentTasks();
            model.addAttribute("parents", parents);
            model.addAttribute(getEntityName(), mapper.toDto(task));
        }
        return getEntityName() + "/update";
    }

    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("parents", service.getAllAvailableParentTasks());
        return super.create(model);
    }
}
