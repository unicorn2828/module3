package com.epam.esm.service.impl;

import com.epam.esm.builder.QueryBuilder;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatePriceDto;
import com.epam.esm.dto.CertificatesDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.ICertificateRepository;
import com.epam.esm.repository.ITagRepository;
import com.epam.esm.service.DataProcessingService;
import com.epam.esm.service.ICertificateService;
import com.epam.esm.validation.CertificateValidator;
import com.epam.esm.validation.CommonValidator;
import com.epam.esm.validation.TagValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.*;
import static com.epam.esm.service.DataProcessingService.PageParamType.PAGE_NUMBER;
import static com.epam.esm.service.DataProcessingService.PageParamType.PAGE_SIZE;

@Slf4j
@Service("certificateService")
@RequiredArgsConstructor
public class CertificateService implements ICertificateService {
    private final ICertificateRepository certificateRepository;
    private final CertificateValidator validator;
    private final DataProcessingService service;
    private final ITagRepository tagRepository;
    private final CommonValidator commonValidator;
    private final TagValidator tagValidator;
    private final QueryBuilder queryBuilder;
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public CertificateDto find(Long id) {
        validator.isId(id);
        Certificate certificate = certificateRepository.find(id)
                                                       .orElseThrow(() -> new ServiceException(CERTIFICATE_WITH_THIS_ID_DOES_NOT_EXIST));
        return mapper.map(certificate, CertificateDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CertificatesDto findAll(Map<String, String> params) {
        params = service.toCamelCase(params);
        int pageNumber = service.receivePageParam(params, PAGE_NUMBER);
        int pageSize = service.receivePageParam(params, PAGE_SIZE);
        List<Certificate> certificates = certificateRepository.findAll(pageNumber,
                                                                       pageSize,
                                                                       queryBuilder.buildQuery(params,
                                                                                               Certificate.class.getSimpleName()));
        List<CertificateDto> certificateDtoList = certificates.stream()
                                                              .map(c -> mapper.map(c, CertificateDto.class))
                                                              .collect(Collectors.toList());
        CertificatesDto certificatesDto = new CertificatesDto();
        certificatesDto.setCertificates(certificateDtoList);
        return certificatesDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
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
        String certificateName = certificateDto.getCertificateName().toLowerCase().trim();
        certificateDto.setCertificateName(certificateName);
        Certificate certificate = certificateRepository.save(mapper.map(certificateDto, Certificate.class));
        log.info("certificate {} created", certificate.getId());
        return mapper.map(certificate, CertificateDto.class);
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
        String certificateName = certificateDto.getCertificateName().toLowerCase().trim();
        certificateDto.setCertificateName(certificateName);
        validator.isCertificate(certificateDto);
        validator.isDate(certificateDto.getCreationDate());
        if (certificateDto.getTags() != null) {
            updateTags(certificateDto);
        }
        Certificate certificate = certificateRepository.update(mapper.map(certificateDto, Certificate.class));
        log.info("certificate {} updated", certificate.getId());
        return mapper.map(certificate, CertificateDto.class);
    }

    @Override
    @Transactional
    public CertificateDto updatePrice(CertificatePriceDto price, Long id) {
        validator.isId(id);
        validator.isNewPrice(price);
        BigDecimal currentPrice = new BigDecimal(price.getNewPrice().trim());
        if (!certificateRepository.find(id).isPresent()) {
            ServiceExceptionCode exception = CERTIFICATE_WITH_THIS_ID_DOES_NOT_EXIST;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
        Certificate certificate = certificateRepository.updatePrice(currentPrice, LocalDate.now(), id);
        log.info("certificate {} price updated", certificate.getId());
        return mapper.map(certificate, CertificateDto.class);
    }

    private void updateTags(CertificateDto certificateDto) {
        tagValidator.isTag(certificateDto.getTags());
        certificateDto.getTags()
                      .stream()
                      .filter(tag -> !tagRepository.findByName(tag.getTagName()
                                                                  .toLowerCase()
                                                                  .trim())
                                                   .isPresent())
                      .forEach(tag -> tagRepository.save(mapper.map(tag, Tag.class)));
        certificateDto.getTags()
                      .forEach(tagDto -> tagDto.setId(tagRepository.findByName(tagDto.getTagName()
                                                                                     .toLowerCase()
                                                                                     .trim())
                                                                   .get()
                                                                   .getId()));
    }
}
