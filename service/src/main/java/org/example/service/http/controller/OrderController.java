package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.dto.PageResponse;
import org.example.service.dto.orderDto.OrderCreateEditDto;
import org.example.service.dto.orderDto.OrderFilter;
import org.example.service.service.OrderService;
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

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String findAll(Model model,
                          OrderFilter filter,
                          @PageableDefault Pageable pageable) {
        var page = orderService.findAll(filter, pageable);
        model.addAttribute("orders", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "order/orders";
    }

    @GetMapping("/{userId}")
    public String findAllByUserId(Model model,
                                  @PathVariable("userId") Long userId,
                                  OrderFilter filter,
                                  @PageableDefault Pageable pageable) {
        var page = orderService.findAll(filter, pageable);
        model.addAttribute("orders", PageResponse.of(page));
        model.addAttribute("filter", filter);
        model.addAttribute("userId", orderService.findAllByUserId(userId));
        return "order/orders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long orderId, Model model) {
        return orderService.findById(orderId)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "order/orders";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public String create(@ModelAttribute @Validated OrderCreateEditDto order,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/books";
        }
        orderService.create(order);
        redirectAttributes.addAttribute("orderSuccessfullyCreated", "true");
        return "redirect:/books";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute OrderCreateEditDto order,
                         RedirectAttributes redirectAttributes) {
        orderService.returnBook(id, order);
        redirectAttributes.addAttribute("orderSuccessfullyUpdated", "true");
        return "redirect:/orders";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long orderId,
                         RedirectAttributes redirectAttributes) {
        if (!orderService.delete(orderId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        redirectAttributes.addAttribute("orderSuccessfullyDeleted", "true");
        return "redirect:/orders";
    }
}
