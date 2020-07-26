package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatePriceDto;
import com.epam.esm.dto.CertificatesDto;
import com.epam.esm.hateoas.ICertificateHateoas;
import com.epam.esm.service.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Map;

/**
 * This is the CertificateController class.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {
    private final ICertificateService certificateService;
    private final ICertificateHateoas hateoas;

    @GetMapping(value = "/{certificateId}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto findCertificateById(@Valid @PathVariable("certificateId") @Positive final Long certificateId) {
        CertificateDto certificate = certificateService.find(certificateId);
        return hateoas.add(certificate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CertificatesDto findAll(@RequestParam Map<String, String> allParams) {
        CertificatesDto certificates = certificateService.findAll(allParams);
        return hateoas.add(certificates);
    }

    @DeleteMapping(value = "/{certificateId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> removeCertificateById(@Valid @PathVariable("certificateId") @Positive final Long certificateId) {
        certificateService.delete(certificateId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto createCertificate(@RequestBody(required = false) final CertificateDto certificateDto) {
        CertificateDto certificate = certificateService.create(certificateDto);
        return hateoas.add(certificate);
    }

    @ResponseBody
    @PutMapping(value = "/{certificateId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto updateCertificate(@PathVariable("certificateId") final Long certificateId,
                                            @RequestBody(required = false) final CertificateDto certificateDto) {
        CertificateDto certificate = certificateService.update(certificateDto, certificateId);
        return hateoas.add(certificate);
    }

    @ResponseBody
    @PatchMapping(value = "/{certificateId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto updatePrice(@PathVariable("certificateId") final Long certificateId,
                                      @RequestBody(required = false) final CertificatePriceDto price) {
        CertificateDto certificate = certificateService.updatePrice(price, certificateId);
        return hateoas.add(certificate);
    }
}
