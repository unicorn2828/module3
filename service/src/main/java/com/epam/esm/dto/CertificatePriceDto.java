package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificatePriceDto {
    private BigDecimal price;
}
