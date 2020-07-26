package com.epam.esm.service.impl;

import com.epam.esm.builder.QueryBuilder;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.IOrderRepository;
import com.epam.esm.repository.IUserRepository;
import com.epam.esm.service.DataProcessingService;
import com.epam.esm.service.IOrderService;
import com.epam.esm.validation.CommonValidator;
import com.epam.esm.validation.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.ORDER_WITH_THIS_ID_DOES_NOT_EXIST;
import static com.epam.esm.model.Role.ROLE_ADMIN;
import static com.epam.esm.service.DataProcessingService.PageParamType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final CertificateService certificateService;
    private final DataProcessingService dataService;
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final OrderValidator orderValidator;
    private final CommonValidator validator;
    private final QueryBuilder queryBuilder;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public OrderDto create(Authentication authentication, BookingDto bookingDto) {
        orderValidator.isOrder(bookingDto);
        bookingDto.getCertificates().forEach(o -> Long.valueOf(o).longValue());
        List<Long> certificates = bookingDto.getCertificates()
                                            .stream()
                                            .map(o -> Long.valueOf(o).longValue())
                                            .collect(Collectors.toList());
        certificates.forEach(validator::isId);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        BigDecimal totalPrice = new BigDecimal(0);
        OrderDto orderDto = new OrderDto();
        List<CertificateDto> certificateList = new ArrayList<>();
        for (Long id : certificates) {
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
    public OrderDto find(Long orderId) {
        validator.isId(orderId);
        Order order = orderRepository.find(orderId).orElseThrow(() -> new ServiceException(ORDER_WITH_THIS_ID_DOES_NOT_EXIST));
        OrderDto orderDto = mapper.map(order, OrderDto.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        if (order.getOwnerName().equals(user.getLogin()) ||
            authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(ROLE_ADMIN.getAuthority()))) {
            return orderDto;
        } else {
            ServiceExceptionCode exception = ServiceExceptionCode.ACCESS_FORBIDDEN;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
    }

    @Override
    public OrdersDto findAll(Map<String, String> params) {
        params = dataService.toCamelCase(params);
        int pageNumber = dataService.receivePageParam(params, PAGE_NUMBER);
        int pageSize = dataService.receivePageParam(params, PAGE_SIZE);
        List<Order> orders = orderRepository.findAll(pageNumber,
                                                     pageSize,
                                                     queryBuilder.buildQuery(params, Order.class.getSimpleName()));
        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setOrders(orders.stream().map(o -> mapper.map(o, OrderDto.class)).collect(Collectors.toList()));
        return ordersDto;
    }
}
