package edu.school21.info.controller.data;

import edu.school21.info.controller.GenericController;
import edu.school21.info.mapper.RecommendationMapper;
import edu.school21.info.model.dto.RecommendationDto;
import edu.school21.info.model.entity.Recommendation;
import edu.school21.info.service.PeerService;
import edu.school21.info.service.RecommendationService;
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
@RequestMapping("/recommendation")
public class RecommendationController extends GenericController<Recommendation, RecommendationDto, Long> {

    private final RecommendationService service;
    private final RecommendationMapper mapper;
    private final PeerService peerService;

    @Override
    protected Class<RecommendationDto> getDtoClass() {
        return RecommendationDto.class;
    }

    @Override
    protected String getEntityName() {
        return "recommendation";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("peers", peerService.getAllNicknames());
        return super.update(id, model);
    }

    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("peers", peerService.getAllNicknames());
        return super.create(model);
    }
}
