package com.epam.esm.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrdersDto {
    private List<OrderDto> orders;
}
