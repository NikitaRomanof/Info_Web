package edu.school21.info.controller;

import edu.school21.info.mapper.GenericMapper;
import edu.school21.info.model.BaseModel;
import edu.school21.info.model.dto.BaseDto;
import edu.school21.info.service.GenericService;
import edu.school21.info.util.FileConverter;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * @param <M> extends BaseModel
 * @param <D> extends BaseDto
 * @param <K> Primary key type (String/Long etc.)
 */
@Controller
@RequiredArgsConstructor
public abstract class GenericController<M extends BaseModel, D extends BaseDto, K> {

    private Class<D> classType;
    private String entityName;
    private GenericMapper<M, D> mapper;
    private GenericService<M, K> service;

    @GetMapping("")
    public String getAll(Model model) {
        List<M> models = service.getAll();
        List<D> dtos = mapper.toDtos(models);
        model.addAttribute(entityName, dtos);
        return entityName + "/viewAll";
    }

    @GetMapping("/add")
    public String create(Model ignore) {
        return entityName + "/add";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("modelForm") D dto) {
        service.create(mapper.toEntity(dto));
        return "redirect:/" + entityName;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable K id) {
        service.delete(id);
        return "redirect:/" + entityName;
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable K id, Model model) {
        Optional<M> entity = service.getOne(id);
        entity.ifPresent(e -> model.addAttribute(entityName, mapper.toDto(e)));
        return entityName + "/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("modelForm") D dto) {
        service.update(mapper.toEntity(dto));
        return "redirect:/" + entityName;
    }

    @PostMapping(value = "/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        List<D> dtos = FileConverter.convertCsvToList(file, classType);
        service.saveAll(mapper.toEntities(dtos));
        return "redirect:/" + entityName;
    }

    @SneakyThrows
    @RequestMapping(path = "/unload")
    public void handleFileUnload(HttpServletResponse servletResponse) {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\" + entityName + .csv\"");
        FileConverter.writeCsvFromBean(servletResponse.getWriter(), mapper.toDtos(service.getAll()), classType);
    }

    @PostConstruct
    private void init() {
        classType = getDtoClass();
        entityName = getEntityName();
        service = getService();
        mapper = getMapper();
    }

    protected abstract Class<D> getDtoClass();

    protected abstract String getEntityName();

    protected abstract GenericService<M, K> getService();

    protected abstract GenericMapper<M, D> getMapper();
}
