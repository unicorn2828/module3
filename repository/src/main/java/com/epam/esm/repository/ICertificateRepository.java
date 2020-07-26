package com.epam.esm.repository;

import com.epam.esm.model.Certificate;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This is the ICertificateRepository interface with the methods of the CertificateRepository class.
 * It extends IBaseRepository.
 * <p>
 * Please see the {@link IBaseRepository} class for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface ICertificateRepository extends IBaseRepository<Certificate> {

    /**
     * Updates the price of certificate {@link Certificate}.
     *
     * @param price            - price of certificate {@link BigDecimal}
     * @param modificationDate - modification date of certificate {@link LocalDate}
     * @param id               - certificate id
     * @return the current certificate {@link Certificate} after updating.
     */
    Certificate updatePrice(BigDecimal price, LocalDate modificationDate, long id);

    /**
     * Updates the certificate {@link Certificate}.
     *
     * @param certificate - certificate entity {@link Certificate} for updating
     * @return the current certificate {@link Certificate} after updating.
     */
    Certificate update(Certificate certificate);

    /**
     * Removes the certificate {@link Certificate} by id from the db.
     *
     * @param id - certificate id
     */
    void delete(long id);

    /**
     * Checks if a certificate {@link Certificate} is already ordered  by id.
     *
     * @param id - certificate id
     * @return {@code true} if the certificate already ordered.
     */
    boolean isOrdered(long id);
}
