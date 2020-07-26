package com.epam.esm.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SuperTagDto extends TagDto {
    private BigDecimal totalPrice;
}

