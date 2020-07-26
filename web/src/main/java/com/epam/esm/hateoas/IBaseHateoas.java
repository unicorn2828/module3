package com.epam.esm.hateoas;

import com.epam.esm.dto.BaseDto;

/**
 * This is the IBaseHateoas interface with a basic method add() of all hateoas classes.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface IBaseHateoas<T1 extends BaseDto, T2> {

    /**
     * Adds HATEOAS links to the current entity.
     *
     * @param t1 - the current entity to add links
     * @return the current entity with the added links.
     */
    T1 add(T1 t1);

    /**
     * Adds HATEOAS links to the current entities.
     *
     * @param t2 - the current entities to add links
     * @return the current entities with the added links.
     */
    T2 add(T2 t2);
}
