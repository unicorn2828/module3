package com.epam.esm.hateoas;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrdersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class OrderHateoas {
    private final CertificateHateoas certificateHateoas;

    public OrderDto add(OrderDto order) {
        order.getCertificates().forEach(certificate -> certificateHateoas.add(certificate));
        order.add(linkTo(methodOn(OrderController.class).find(order.getId())).withSelfRel());
        return order;
    }

    public OrdersDto add(OrdersDto orders) {
        orders.getOrders().forEach(order -> add(order));
        return orders;
    }
}
