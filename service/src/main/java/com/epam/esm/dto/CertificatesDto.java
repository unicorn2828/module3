package com.epam.esm.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class CertificatesDto extends RepresentationModel<CertificatesDto> {
    private List<CertificateDto> certificates;
}
