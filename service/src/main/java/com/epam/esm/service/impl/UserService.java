package com.epam.esm.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.builder.impl.UserQueryBuilder;
import com.epam.esm.dto.*;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.jwt.JwtTokenProvider;
import com.epam.esm.model.Order;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.repository.IUserRepository;
import com.epam.esm.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.*;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {
    private static final String KEY_TOKEN = "token";
    private static final String KEY_TOKEN_TYPE = "type";
    private static final String KEY_TOKEN_SUBJECT = "sub";
    private static final String KEY_TOKEN_ISSUED_AT = "iat";
    private static final String KEY_TOKEN_EXPIRATION = "exp";
    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String VALUE_TOKEN_TYPE = "bearer";
    private final ModelMapper mapper;
    private final DataProcessingService service;
    private final UserQueryBuilder queryBuilder;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> signIn(AuthUserDto authUserDto) {
        String login = authUserDto.getLogin();
        String password = authUserDto.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (AuthenticationException e) {
            ServiceExceptionCode errorCode = INVALID_LOGIN_OR_PASSWORD;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        UserDetails userDetails = loadUserByUsername(login);
        String token = jwtTokenProvider.createToken(login, userDetails.getAuthorities()
                                                                      .stream()
                                                                      .map(GrantedAuthority::getAuthority)
                                                                      .collect(Collectors.toList()));
        String refreshToken = jwtTokenProvider.createRefreshToken(login, userDetails.getAuthorities()
                                                                                    .stream()
                                                                                    .map(GrantedAuthority::getAuthority)
                                                                                    .collect(Collectors.toList()));
        DecodedJWT decodedJWT = JWT.decode(token);
        Map<String, String> model = new LinkedHashMap<>();
        model.put(KEY_TOKEN_SUBJECT, decodedJWT.getSubject());
        model.put(KEY_TOKEN_ISSUED_AT, Long.toString(decodedJWT.getIssuedAt().getTime()));
        model.put(KEY_TOKEN_EXPIRATION, Long.toString(decodedJWT.getExpiresAt().getTime()));
        model.put(KEY_TOKEN_TYPE, VALUE_TOKEN_TYPE);
        model.put(KEY_TOKEN, token);
        model.put(KEY_REFRESH_TOKEN, refreshToken);
        return ok(model);
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = userRepository.findByLogin(login)
                                  .orElseThrow(() -> new ServiceException(USER_WITH_THIS_LOGIN_DOES_NOT_EXIST));
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(user.getId());
        userDetails.setLogin(user.getLogin());
        userDetails.setPassword(user.getPassword());
        userDetails.setEmail(user.getEmail());
        userDetails.setAuthorities(user.getRole()
                                       .stream()
                                       .map(role -> new SimpleGrantedAuthority(role.name()))
                                       .collect(Collectors.toList()));
        return userDetails;
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            ServiceExceptionCode errorCode = USER_WITH_THE_SAME_LOGIN_ALREADY_EXISTS;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        } else if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
            User user = new User();
            user.setLogin(userDto.getLogin());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.getRole().add(Role.ROLE_USER);
            user = userRepository.save(user);
            log.info("user {} registered", user.getLogin());
            return mapper.map(user, UserDto.class);
        } else {
            ServiceExceptionCode exception = PASSWORD_AND_CONFIRM_ARE_NOT_EQUAL;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
    }

    @Override
    public UserDto find(long id) {
        User user = userRepository.find(id)
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
    public OrdersDto findUserOrders(long id) {
        User user = userRepository.find(id)
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
    public UsersDto findAll(Map<String, String> allParams) {
        Map<String, String> params = service.toLowerCase(allParams);
        int pageNumber = service.receivePageNumber(params);
        int pageSize = service.receivePageSize(params);
        List<User> userList = userRepository.findAll(pageNumber, pageSize, queryBuilder.buildQuery(params));
        UsersDto usersDto = new UsersDto();
        usersDto.setUsers(userList.stream()
                               .map(user -> mapper.map(user, UserDto.class))
                               .collect(Collectors.toList()));
        return usersDto;
    }
}
