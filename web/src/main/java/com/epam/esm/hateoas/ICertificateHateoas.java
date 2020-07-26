package com.epam.esm.hateoas;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatesDto;

/**
 * This is the ICertificateHateoas interface; it extends {@link IBaseHateoas} interface.
 * <p>
 * Please see the {@link IBaseHateoas} class for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface ICertificateHateoas extends IBaseHateoas<CertificateDto, CertificatesDto> {
}
