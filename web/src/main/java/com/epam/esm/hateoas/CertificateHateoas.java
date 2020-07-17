package com.epam.esm.hateoas;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.BookingDto;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatesDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CertificateHateoas {

    public CertificateDto add(CertificateDto certificate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BookingDto bookingDto = new BookingDto();
        bookingDto.setCertificates(List.of(certificate.getId()));
        if (certificate.getTags() != null) {
            certificate.getTags()
                       .forEach(tag -> tag.add(linkTo(methodOn(TagController.class).find(tag.getId())).withSelfRel()));
        }
        certificate.add(linkTo(methodOn(CertificateController.class).find(certificate.getId())).withSelfRel());
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") ||
                                                                   r.getAuthority().equals("ROLE_USER"))) {
            certificate.add(linkTo(methodOn(OrderController.class).create(authentication, bookingDto)).withRel("booking"));
        }
        if (authentication.getAuthorities().stream()
                          .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            certificate.add(linkTo(methodOn(CertificateController.class).remove(certificate.getId())).withRel("delete"));
        }
        return certificate;
    }

    public CertificatesDto add(CertificatesDto certificates) {
        certificates.getCertificates().forEach(certificate -> add(certificate));
        return certificates;
    }
}
