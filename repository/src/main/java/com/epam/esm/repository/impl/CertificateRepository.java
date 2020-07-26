package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.ICertificateRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This is the CertificateRepository class; it  extends {@link BaseAbstractRepository} class.
 * <p>
 * Please see the {@link BaseAbstractRepository} and {@link ICertificateRepository} classes for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Repository
public class CertificateRepository extends BaseAbstractRepository<Certificate> implements ICertificateRepository {
    private static final String CERTIFICATE_PRICE = "price";
    private static final String CERTIFICATE_MODIFICATION_DATE = "modificationDate";
    private static final String CERTIFICATE_ID = "id";
    private static final String UPDATE_PRICE =
            "UPDATE Certificate c SET c.price = :price, c.modificationDate = :modificationDate WHERE c.id = :id";
    private static final String CERTIFICATE_IS_ORDERED =
            "SELECT * FROM order_certificate WHERE order_certificate.certificate_id = ?";

    @PersistenceContext
    private EntityManager entityManager;

    public CertificateRepository(EntityManager em) {
        super(em, Certificate.class);
    }

    @Override
    public Certificate updatePrice(BigDecimal price, LocalDate modificationDate, long id) {
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
