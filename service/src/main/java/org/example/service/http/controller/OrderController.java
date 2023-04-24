package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.dto.PageResponse;
import org.example.service.dto.orderDto.OrderFilter;
import org.example.service.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String findAll(Model model,
                          OrderFilter filter,
                          Pageable pageable) {
        var page = orderService.findAll(filter, pageable);
        model.addAttribute("orders", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "order/orders";
    }

    @GetMapping("/{id}")
    public String orderFindById(@PathVariable("id") Long orderId, Model model) {
        return orderService.findById(orderId)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "order/order";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
