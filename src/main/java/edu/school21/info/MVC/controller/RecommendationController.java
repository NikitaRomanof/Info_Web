package edu.school21.info.MVC.controller;


import edu.school21.info.mapper.RecommendationMapper;
import edu.school21.info.model.dto.RecommendationDto;
import edu.school21.info.model.entity.Recommendation;
import edu.school21.info.service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService service;
    private final RecommendationMapper mapper;

    public RecommendationController(RecommendationService service, RecommendationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<Recommendation> recommendationEntity = service.listAll();
        List<RecommendationDto> recommendationDtos = mapper.toDtos(recommendationEntity);
        model.addAttribute("recommendation", recommendationDtos);
        return "recommendation/viewAllRecommendation";
    }

    @GetMapping("/add")
    public String create() {
        return "recommendation/addRecommendation";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("checkRecommendation") RecommendationDto recommendationDto) {
        service.create(mapper.toEntity(recommendationDto));
        return "redirect:/recommendation";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/recommendation";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<Recommendation> recommendationEntityOptional = service.getOne(id);
        recommendationEntityOptional.ifPresent(e -> model.addAttribute("recommendation", mapper.toDto(e)));
        return "recommendation/updateRecommendation";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("recommendationForm") RecommendationDto recommendationDto) {
        service.update(mapper.toEntity(recommendationDto));
        return "redirect:/recommendation";
    }
}
