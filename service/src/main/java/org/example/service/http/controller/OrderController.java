package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.database.repository.OrderRepository;
import org.example.service.dto.PageResponse;
import org.example.service.dto.orderDto.OrderFilter;
import org.example.service.service.BookService;
import org.example.service.service.OrderService;
import org.example.service.service.UserService;
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
    private final UserService userService;
    private final BookService bookService;
    private final OrderRepository orderRepository;

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
//
//    @GetMapping
//    public String addOrder(Model model,
//                          @ModelAttribute("order") OrderCreateEditDto order) {
//        model.addAttribute("order", order);
//        model.addAttribute("users", userService.findAll());
//        model.addAttribute("books", bookService.findAll());
//        return "order/addOrder";
//    }
//
//    @PostMapping
//    public String create(@ModelAttribute OrderCreateEditDto order,
//                         BindingResult bindingResult,
//                         RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("order", order);
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
//            return "redirect:/orders";
//        }
//        orderService.create(order);
//        return "redirect:/books";
//    }
//
//    @GetMapping("/{id}/update")
//    public String update(@PathVariable(value = "id") long orderId, Model model) {
//        if (!orderRepository.existsById(orderId)) {
//            return "redirect:/orders";
//        }
//        Optional<Order> order = orderRepository.findById(orderId);
//        List<Order> result = new ArrayList<>();
//        order.ifPresent(result::add);
//        model.addAttribute("users", userService.findAll());
//        model.addAttribute("book", bookService.findAll());
//        model.addAttribute("order", result);
//        return "order/editOrder";
//    }
//
//    @PostMapping("/{id}/update")
//    public String update(@PathVariable("id") Long id,
//                         @ModelAttribute OrderCreateEditDto order) {
//        return orderService.update(id, order)
//                .map(it -> "redirect:/orders/{id}")
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//    }
//
//    @PostMapping("/{id}/delete")
//    public String delete(@PathVariable("id") Long orderId) {
//        if (!orderService.delete(orderId)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        return "redirect:/orders";
//    }
}
