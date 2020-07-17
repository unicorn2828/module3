package com.epam.esm.repository;

import com.epam.esm.model.Certificate;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ICertificateRepository extends IBaseRepository<Certificate> {

    Certificate updatePrice(BigDecimal price, LocalDate date, Long id);

    Certificate update(Certificate c);

    void delete(long id);

    boolean isOrdered(long id);
}
