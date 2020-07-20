package com.epam.esm.dto;

import lombok.Data;

import java.util.List;

@Data
public class CertificatesDto {
    private List<CertificateDto> certificates;
}
