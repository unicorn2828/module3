package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.OrderQueryBuilder;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.IOrderRepository;
import com.epam.esm.repository.IUserRepository;
import com.epam.esm.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.ORDER_WITH_THIS_ID_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService {
    private final CertificateService certificateService;
    private final IOrderRepository orderRepository;
    private final OrderQueryBuilder queryBuilder;
    private final IUserRepository userRepository;
    private final DataProcessingService service;
    private final ModelMapper mapper;

    @Override
    public OrderDto create(Authentication authentication, BookingDto bookingDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        BigDecimal totalPrice = new BigDecimal(0);
        OrderDto orderDto = new OrderDto();
        List<CertificateDto> certificateList = new ArrayList<>();
        for (Long id : bookingDto.getCertificates()) {
            CertificateDto certificate = certificateService.find(id);
            totalPrice = totalPrice.add(new BigDecimal(String.valueOf(certificate.getPrice())));
            certificateList.add(certificate);
            orderDto.setCertificates(certificateList);
        }
        User user = userRepository.find(userDetails.getId()).get();
        UserDto userDto = mapper.map(user, UserDto.class);
        orderDto.setOwnerName(userDto.getLogin());
        orderDto.setUserDto(userDto);
        orderDto.setCreationDate(LocalDate.now());
        orderDto.setOrderPrice(totalPrice);
        Order order = orderRepository.save(mapper.map(orderDto, Order.class));
        return mapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto find(long id) {
        Order order = orderRepository.find(id).orElseThrow(() -> new ServiceException(ORDER_WITH_THIS_ID_DOES_NOT_EXIST));
        OrderDto orderDto = mapper.map(order, OrderDto.class);
        return orderDto;
    }

    @Override
    public OrdersDto findAll(Map<String, String> allParams) {
        Map<String, String> params = service.toLowerCase(allParams);
        int pageNumber = service.receivePageNumber(params);
        int pageSize = service.receivePageSize(params);
        List<Order> orders = orderRepository.findAll(pageNumber,
                                                     pageSize,
                                                     queryBuilder.buildQuery(params));
        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setOrders(orders.stream().map(o -> mapper.map(o, OrderDto.class)).collect(Collectors.toList()));
        return ordersDto;
    }
}
