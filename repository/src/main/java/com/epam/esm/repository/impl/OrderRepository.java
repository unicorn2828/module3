package com.epam.esm.repository.impl;

import com.epam.esm.model.Order;
import com.epam.esm.repository.IOrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository implements IOrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> find(long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
  }

    @Override
    public Order save(Order order) {
        System.out.println(order);
        entityManager.persist(order);
        entityManager.flush();
        return entityManager.find(Order.class, order.getId());
    }

    @Override
    public List<Order> findAll(int pageNumber, int pageSize, String sql){
        TypedQuery<Order> query = entityManager.createQuery(sql, Order.class);
        query.setFirstResult((pageNumber-1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();}
}
