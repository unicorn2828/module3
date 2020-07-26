package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatePriceDto;
import com.epam.esm.dto.CertificatesDto;

/**
 * The ICertificateService interface; it extends {@link IBaseService} class.
 * <p>
 * Please see the {@link IBaseService} class for true identity.
 *
 * @author Vitaly Kononov
 */
public interface ICertificateService extends IBaseService<CertificateDto, CertificatesDto> {

    /**
     * Removes certificate by id.
     *
     * @param certificateId - id of certificate
     */
    void delete(final Long certificateId);

    /**
     * Creates certificate.
     *
     * @param certificateDto - certificate for saving to db
     * @return the current certificate after saving.
     */
    CertificateDto create(final CertificateDto certificateDto);

    /**
     * Updates current certificate by id.
     *
     * @param certificateDto - certificate for updating
     * @param certificateId - certificate id for correct updating
     * @return the current certificate after updating.
     */
    CertificateDto update(CertificateDto certificateDto, final Long certificateId);

    /**
     * Updates the price of certificate by id.
     *
     * @param price - new price of certificate
     * @param certificateId - certificate id for correct updating
     * @return the current certificate after updating.
     */
    CertificateDto updatePrice(CertificatePriceDto price, final Long certificateId);
}
