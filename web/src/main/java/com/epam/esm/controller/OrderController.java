package com.epam.esm.controller;

import com.epam.esm.dto.BookingDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrdersDto;
import com.epam.esm.hateoas.IOrderHateoas;
import com.epam.esm.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * This is the OrderController class.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    private final IOrderService orderService;
    private final IOrderHateoas hateoas;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto find(@PathVariable("id") final Long id) {
        OrderDto order = orderService.find(id);
        return hateoas.add(order);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public OrdersDto findAll(@RequestParam Map<String, String> allParams) {
        OrdersDto orders = orderService.findAll(allParams);
        return hateoas.add(orders);
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(Authentication authentication,
                           @Valid @RequestBody(required = false) final BookingDto bookingDto) {
        OrderDto order =orderService.create(authentication, bookingDto);
        return hateoas.add(order);
    }
}
