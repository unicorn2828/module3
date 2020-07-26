package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CertificateDto extends BaseDto {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private String certificateName;
    private String description;
    private BigDecimal price;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDate creationDate;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDate modificationDate;
    private Integer duration;
    private List<TagDto> tags;
}
