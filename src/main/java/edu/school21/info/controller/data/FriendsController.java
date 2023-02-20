package edu.school21.info.controller.data;

import edu.school21.info.controller.GenericController;
import edu.school21.info.mapper.FriendsMapper;
import edu.school21.info.model.dto.FriendsDto;
import edu.school21.info.model.entity.Friends;
import edu.school21.info.service.FriendsService;
import edu.school21.info.service.PeerService;
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
@RequestMapping("/friends")
public class FriendsController extends GenericController<Friends, FriendsDto, Long> {

    private final FriendsService service;
    private final FriendsMapper mapper;
    private final PeerService peerService;

    @Override
    protected Class<FriendsDto> getDtoClass() {
        return FriendsDto.class;
    }

    @Override
    protected String getEntityName() {
        return "friends";
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