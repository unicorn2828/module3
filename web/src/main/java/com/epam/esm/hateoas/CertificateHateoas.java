package com.epam.esm.hateoas;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.BookingDto;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.model.Role.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class CertificateHateoas {
    private static final String BOOKING_OPERATION = "booking";
    private static final String DELETE_OPERATION = "delete";
    private final TagHateoas tagHateoas;

    public CertificateDto add(CertificateDto certificate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookingDto bookingDto = new BookingDto();
        bookingDto.setCertificates(List.of(certificate.getId()));
        if (certificate.getTags() != null) {
            certificate.getTags().forEach(tag -> tagHateoas.add(tag));
        }
        certificate.add(linkTo(methodOn(CertificateController.class).find(certificate.getId())).withSelfRel());
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(ROLE_ADMIN.getAuthority()) ||
                                                                   r.getAuthority().equals(ROLE_USER.getAuthority()))) {
            certificate.add(linkTo(methodOn(OrderController.class).create(authentication, bookingDto)).withRel(BOOKING_OPERATION));
        }
        if (authentication.getAuthorities()
                          .stream()
                          .anyMatch(r -> r.getAuthority().equals(ROLE_ADMIN.getAuthority()))) {
            certificate.add(linkTo(methodOn(CertificateController.class).remove(certificate.getId())).withRel(DELETE_OPERATION));
        }
        return certificate;
    }

    public CertificatesDto add(CertificatesDto certificates) {
        certificates.getCertificates().forEach(certificate -> add(certificate));
        return certificates;
    }
}
