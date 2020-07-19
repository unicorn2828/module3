package com.epam.esm.service.impl;

import com.epam.esm.builder.QueryBuilder;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatePriceDto;
import com.epam.esm.dto.CertificatesDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.mapper.DtoMapper;
import com.epam.esm.model.Certificate;
import com.epam.esm.repository.ICertificateRepository;
import com.epam.esm.repository.ITagRepository;
import com.epam.esm.service.DataProcessingService;
import com.epam.esm.service.ICertificateService;
import com.epam.esm.validation.CertificateValidator;
import com.epam.esm.validation.TagValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.CERTIFICATE_IS_ORDERED;
import static com.epam.esm.exception.ServiceExceptionCode.CERTIFICATE_WITH_THIS_ID_DOES_NOT_EXIST;

@Slf4j
@Service("certificateService")
@RequiredArgsConstructor
public class CertificateService implements ICertificateService {
    private final ICertificateRepository certificateRepository;
    private final CertificateValidator validator;
    private final DataProcessingService service;
    private final ITagRepository tagRepository;
    private final TagValidator tagValidator;
    private final QueryBuilder queryBuilder;
    private final DtoMapper mapper;

    @Override
    public CertificateDto find(long id) {
        validator.isId(id);
        Certificate certificate = certificateRepository.find(id)
                                                       .orElseThrow(() -> new ServiceException(CERTIFICATE_WITH_THIS_ID_DOES_NOT_EXIST));
        return mapper.toCertificateDto(certificate);
    }

    @Override
    public CertificatesDto findAll(Map<String, String> params) {
        params = service.toCamelCase(params);
        int pageNumber = service.receivePageNumber(params);
        int pageSize = service.receivePageSize(params);
        List<Certificate> certificates = certificateRepository.findAll(pageNumber,
                                                                       pageSize,
                                                                       queryBuilder.buildQuery(params,
                                                                                               Certificate.class.getSimpleName()));
        List<CertificateDto> certificateDtoList = certificates.stream()
                                                              .map(mapper::toCertificateDto)
                                                              .collect(Collectors.toList());
        CertificatesDto certificatesDto = new CertificatesDto();
        certificatesDto.setCertificates(certificateDtoList);
        return certificatesDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        validator.isId(id);
        if (certificateRepository.find(id).isPresent()) {
            if (!certificateRepository.isOrdered(id)) {
                certificateRepository.delete(id);
                log.info("certificate {} deleted", id);
            } else {
                ServiceExceptionCode exception = CERTIFICATE_IS_ORDERED;
                log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
                throw new ServiceException(exception);
            }
        } else {
            ServiceExceptionCode exception = CERTIFICATE_WITH_THIS_ID_DOES_NOT_EXIST;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
    }

    @Override
    @Transactional
    public CertificateDto create(CertificateDto certificateDto) {
        validator.isCertificate(certificateDto);
        certificateDto.setCreationDate(LocalDate.now());
        certificateDto.setModificationDate(LocalDate.now());
        if (certificateDto.getTags() != null) {
            updateTags(certificateDto);
        }
        Certificate certificate = certificateRepository.save(mapper.toCertificate(certificateDto));
        log.info("certificate {} created", certificate.getId());
        return mapper.toCertificateDto(certificate);
    }

    @Override
    @Transactional
    public CertificateDto update(CertificateDto certificateDto, Long id) {
        validator.isId(id);
        if (!certificateRepository.find(id).isPresent()) {
            ServiceExceptionCode exception = CERTIFICATE_WITH_THIS_ID_DOES_NOT_EXIST;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
        certificateDto.setId(id);
        certificateDto.setModificationDate(LocalDate.now());
        validator.isDate(certificateDto.getCreationDate());
        if (certificateDto.getTags() != null) {
            updateTags(certificateDto);
        }
        Certificate certificate = certificateRepository.update(mapper.toCertificate(certificateDto));
        log.info("certificate {} updated", certificate.getId());
        return mapper.toCertificateDto(certificate);
    }

    @Override
    @Transactional
    public CertificateDto updatePrice(CertificatePriceDto price, Long id) {
        validator.isId(id);
        validator.isPrice(price.getPrice());
        BigDecimal newPrice = price.getPrice();
        if (!certificateRepository.find(id).isPresent()) {
            ServiceExceptionCode exception = CERTIFICATE_WITH_THIS_ID_DOES_NOT_EXIST;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
        Certificate certificate = certificateRepository.updatePrice(newPrice, LocalDate.now(), id);
        log.info("certificate {} price updated", certificate.getId());
        return mapper.toCertificateDto(certificate);
    }

    private void updateTags(CertificateDto certificateDto) {
        certificateDto.getTags()
                      .forEach(tagValidator::isTag);
        certificateDto.getTags()
                      .stream()
                      .filter(c -> !tagRepository.findByName(c.getTagName()).isPresent())
                      .forEach(c -> tagRepository.save(mapper.toTag(c)));
        certificateDto.getTags()
                      .forEach(tagDto -> tagDto.setId(tagRepository.findByName(tagDto.getTagName())
                                                                   .get()
                                                                   .getId()));
    }
}
