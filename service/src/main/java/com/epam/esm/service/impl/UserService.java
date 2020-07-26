package com.epam.esm.service.impl;

import com.epam.esm.builder.QueryBuilder;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.model.Order;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.repository.IUserRepository;
import com.epam.esm.service.DataProcessingService;
import com.epam.esm.service.IUserService;
import com.epam.esm.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.ACCESS_FORBIDDEN;
import static com.epam.esm.exception.ServiceExceptionCode.USER_WITH_THIS_ID_DOES_NOT_EXIST;
import static com.epam.esm.service.DataProcessingService.PageParamType.PAGE_NUMBER;
import static com.epam.esm.service.DataProcessingService.PageParamType.PAGE_SIZE;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final ModelMapper mapper;
    private final UserValidator validator;
    private final QueryBuilder queryBuilder;
    private final DataProcessingService service;
    private final IUserRepository userRepository;

    @Override
    public UserDto find(Long userId) {
        validator.isId(userId);
        User user = userRepository.find(userId)
                                  .orElseThrow(() -> new ServiceException(USER_WITH_THIS_ID_DOES_NOT_EXIST));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        String userRole = new ArrayList(currentUser.getAuthorities()).get(0).toString();
        if (Role.ROLE_ADMIN.toString().equals(userRole) || currentUser.getId() == user.getId()) {
            user.setOrders(null);
            return mapper.map(user, UserDto.class);
        } else {
            ServiceExceptionCode exception = ACCESS_FORBIDDEN;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
    }

    @Override
    public OrdersDto findUserOrders(Long userId) {
        validator.isId(userId);
        User user = userRepository.find(userId)
                                  .orElseThrow(() -> new ServiceException(USER_WITH_THIS_ID_DOES_NOT_EXIST));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        String userRole = new ArrayList(currentUser.getAuthorities()).get(0).toString();
        if (Role.ROLE_ADMIN.toString().equals(userRole) || currentUser.getId() == user.getId()) {
            List<Order> orders = user.getOrders();
            List<OrderDto> ordersDtoList = orders.stream()
                                                 .map(order -> mapper.map(order, OrderDto.class))
                                                 .collect(Collectors.toList());
            OrdersDto ordersDto = new OrdersDto();
            ordersDto.setOrders(ordersDtoList);
            return ordersDto;
        } else {
            ServiceExceptionCode exception = ACCESS_FORBIDDEN;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
    }

    @Override
    public UsersDto findAll(Map<String, String> params) {
        params = service.toCamelCase(params);
        int pageNumber = service.receivePageParam(params, PAGE_NUMBER);
        int pageSize = service.receivePageParam(params, PAGE_SIZE);
        List<User> userList = userRepository.findAll(pageNumber,
                                                     pageSize,
                                                     queryBuilder.buildQuery(params, User.class.getSimpleName()));
        UsersDto usersDto = new UsersDto();
        usersDto.setUsers(userList.stream()
                                  .map(user -> mapper.map(user, UserDto.class))
                                  .collect(Collectors.toList()));
        return usersDto;
    }

    @Override
    public TagsDto findUserSuperTag(Long userId) {
        validator.isId(userId);
        OrdersDto userOrders = findUserOrders(userId);
        List<TagDto> userTagList = SuperTagService.findUserTags(userOrders);
        List<TagDto> superTagList = SuperTagService.findSuperTagList(userTagList, userOrders);
        TagsDto tagsDto = new TagsDto();
        tagsDto.setTags(superTagList);
        return tagsDto;
    }
}
