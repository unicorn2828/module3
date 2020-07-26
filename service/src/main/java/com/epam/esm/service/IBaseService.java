package com.epam.esm.service;

import com.epam.esm.dto.BaseDto;

import java.util.Map;

/**
 * This is the IBaseService interface with the basic methods find() and findAll().
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface IBaseService<T1 extends BaseDto, T2> {

    /**
     * Finds entity by id.
     *
     * @param id - id of entity
     * @return the current entity.
     */
    T1 find(final Long id);

    /**
     * Finds all dto entities by the params.
     *
     * @param allParams - list of params for searching
     * @return the current dto entities.
     */
    T2 findAll(Map<String, String> allParams);
}
