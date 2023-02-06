package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.TransferredPointsMapper;
import edu.school21.info.model.dto.TransferredPointsDto;
import edu.school21.info.model.entity.TransferredPoints;
import edu.school21.info.service.TransferredPointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/transferredPoints")
public class TransferredPointsController {
    private final TransferredPointsService service;
    private final TransferredPointsMapper mapper;

    public TransferredPointsController(TransferredPointsService service, TransferredPointsMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<TransferredPoints> transferredPointsEntity = service.listAll();
        List<TransferredPointsDto> transferredPointsDtos = mapper.toDtos(transferredPointsEntity);
        model.addAttribute("transferredPoints", transferredPointsDtos);
        return "transferredPoints/viewAllTransferredPoints";
    }

    @GetMapping("/add")
    public String create() {
        return "transferredPoints/addTransferredPoints";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("transferredPointsForm") TransferredPointsDto transferredPointsDto) {
        service.create(mapper.toEntity(transferredPointsDto));
        return "redirect:/transferredPoints";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/transferredPoints";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<TransferredPoints> transferredPointsEntityOptional = service.getOne(id);
        transferredPointsEntityOptional.ifPresent(e -> model.addAttribute("transferredPoints", mapper.toDto(e)));
        return "transferredPoints/updateTransferredPoints";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("transferredPointsForm") TransferredPointsDto transferredPointsDto) {
        service.update(mapper.toEntity(transferredPointsDto));
        return "redirect:/transferredPoints";
    }
}
