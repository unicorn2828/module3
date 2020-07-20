package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto extends BaseDto {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private BigDecimal orderPrice;
    private String ownerName;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDate creationDate;
    private UserDto userDto;
    private List<CertificateDto> certificates;
}
