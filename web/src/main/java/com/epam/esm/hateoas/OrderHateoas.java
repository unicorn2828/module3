package com.epam.esm.hateoas;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrdersDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderHateoas {

    public OrderDto add(OrderDto order) {
        order.getCertificates()
                   .forEach(certificate -> certificate.add(linkTo(methodOn(CertificateController.class)
                                                                          .find(certificate.getId())).withSelfRel()));
        order.add(linkTo(methodOn(CertificateController.class).find(order.getId())).withSelfRel());
        return order;
    }

    public OrdersDto add(OrdersDto orders) {
        orders.getOrders().forEach(order -> add(order));
        return orders;
    }
}
