package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Role;
import org.example.service.dto.PageResponse;
import org.example.service.dto.userDto.UserCreateEditDto;
import org.example.service.dto.userDto.UserFilter;
import org.example.service.service.UserService;
import org.example.service.validation.group.CreateAction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.groups.Default;
import java.util.ArrayList;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String findAll(Model model,
                          UserFilter filter,
                          @PageableDefault Pageable pageable) {
        var page = userService.findAll(filter, pageable);
        model.addAttribute("users", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "user/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id,
                           Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model,
                               @ModelAttribute("user") UserCreateEditDto user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user/registration";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated({Default.class, CreateAction.class}) UserCreateEditDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/registration";
        }
        userService.create(user);
        redirectAttributes.addAttribute("userSuccessfullyCreated", "true");
        return "redirect:/users/registration";
    }

    @GetMapping("/{id}/update")
    public String userEdit(@PathVariable(value = "id") Long userId,
                           Model model) {
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return "redirect:/books";
        }
        var result = new ArrayList<>();
        user.ifPresent(result::add);
        model.addAttribute("roles", Role.values());
        model.addAttribute("user", result);
        return "user/user";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Validated UserCreateEditDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/{id}/update";
        }

        userService.update(id, user);
        redirectAttributes.addAttribute("userSuccessfullyUpdated", "true");
        return "redirect:/users/{id}/update";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id,
                         RedirectAttributes redirectAttributes) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        redirectAttributes.addAttribute("userSuccessfullyDeleted", "true");
        return "redirect:/users";
    }
}
