package com.epam.esm.service;

import com.epam.esm.dto.BaseDto;

import java.util.Map;

public interface IBaseService<T1 extends BaseDto, T2> {

    T1 find(final long id);

    T2 findAll(Map<String, String> allParams);
}
