package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrdersDto;
import com.epam.esm.hateoas.IOrderHateoas;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class OrderHateoas implements IOrderHateoas {
    private final CertificateHateoas certificateHateoas;

    @Override
    public OrderDto add(OrderDto order) {
        order.getCertificates().forEach(certificate -> certificateHateoas.add(certificate));
        order.add(linkTo(methodOn(OrderController.class).find(order.getId())).withSelfRel());
        return order;
    }

    @Override
    public OrdersDto add(OrdersDto orders) {
        orders.getOrders().forEach(order -> add(order));
        orders.add(linkTo(methodOn(OrderController.class).findAll(new HashMap<>())).withSelfRel());
        return orders;
    }
}
