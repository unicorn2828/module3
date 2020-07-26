package com.epam.esm.repository.impl;

import com.epam.esm.model.Order;
import com.epam.esm.repository.IOrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * This is the OrderRepository class; it  extends @link BaseAbstractRepository} class.
 * <p>
 * Please see the {@link BaseAbstractRepository} and {@link IOrderRepository} classes for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Repository
public class OrderRepository extends BaseAbstractRepository<Order> implements IOrderRepository {

    public OrderRepository(EntityManager em) {
        super(em, Order.class);
    }
}