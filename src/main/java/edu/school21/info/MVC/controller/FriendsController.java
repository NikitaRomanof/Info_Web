package edu.school21.info.MVC.controller;

import edu.school21.info.mapper.FriendsMapper;
import edu.school21.info.model.dto.FriendsDto;
import edu.school21.info.model.entity.Friends;
import edu.school21.info.service.FriendsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/friends")
public class FriendsController {

    private final FriendsService service;
    private final FriendsMapper mapper;

    public FriendsController(FriendsService service, FriendsMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<Friends> friendsEntity = service.listAll();
        List<FriendsDto> friendsDtos = mapper.toDtos(friendsEntity);
        model.addAttribute("friends", friendsDtos);
        return "friends/viewAllFriends";
    }

    @GetMapping("/add")
    public String create() {
        return "friends/addFriends";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("friendsForm") FriendsDto friendsDto) {
        service.create(mapper.toEntity(friendsDto));
        return "redirect:/friends";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/friends";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<Friends> friendsEntityOptional = service.getOne(id);
        friendsEntityOptional.ifPresent(e -> model.addAttribute("friends", mapper.toDto(e)));
        return "friends/updateFriends";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("friendsForm") FriendsDto friendsDto) {
        service.update(mapper.toEntity(friendsDto));
        return "redirect:/friends";
    }
}
