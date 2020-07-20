package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.ICertificateRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateRepository implements ICertificateRepository {
    private static final String CERTIFICATE_PRICE = "price";
    private static final String CERTIFICATE_MODIFICATION_DATE = "modificationDate";
    private static final String CERTIFICATE_ID = "id";
    private static final String UPDATE_PRICE = "UPDATE Certificate c " +
                                               "SET c.price = :price, c.modificationDate = :modificationDate " +
                                               "WHERE c.id = :id";
    private static final String CERTIFICATE_IS_ORDERED = "SELECT * " +
                                                         "FROM order_certificate " +
                                                         "WHERE order_certificate.certificate_id = ?";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List findAll(int pageNumber, int pageSize, String sql) {
        TypedQuery<Certificate> query = entityManager.createQuery(sql, Certificate.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public Certificate updatePrice(BigDecimal price, LocalDate modificationDate, Long id) {
        Query query = entityManager.createQuery(UPDATE_PRICE);
        query.setParameter(CERTIFICATE_PRICE, price);
        query.setParameter(CERTIFICATE_MODIFICATION_DATE, modificationDate);
        query.setParameter(CERTIFICATE_ID, id);
        query.executeUpdate();
        entityManager.flush();
        entityManager.clear();
        return entityManager.find(Certificate.class, id);
    }

    @Override
    public Certificate update(Certificate certificate) {
        entityManager.merge(certificate);
        return entityManager.find(Certificate.class, certificate.getId());
    }

    @Override
    public Optional<Certificate> find(long id) {
        return Optional.ofNullable(entityManager.find(Certificate.class, id));
    }

    @Override
    public Certificate save(Certificate certificate) {
        entityManager.persist(certificate);
        entityManager.flush();
        return entityManager.find(Certificate.class, certificate.getId());
    }

    @Override
    public void delete(long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        entityManager.remove(certificate);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public boolean isOrdered(long id) {
        Query query = entityManager.createNativeQuery(CERTIFICATE_IS_ORDERED);
        query.setParameter(1, id);
        return query.getResultList().isEmpty() ? false : true;
    }
}
