package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatesDto;
import com.epam.esm.dto.CertificatePriceDto;
import com.epam.esm.hateoas.CertificateHateoas;
import com.epam.esm.service.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {
    private final ICertificateService certificateService;
    private final CertificateHateoas hateoas;

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto find(@PathVariable("id") final Long id) {
        CertificateDto certificate = certificateService.find(id);
        return hateoas.add(certificate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CertificatesDto findAll(@RequestParam Map<String, String> allParams) {
        CertificatesDto certificates = certificateService.findAll(allParams);
        return hateoas.add(certificates);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> remove(@PathVariable("id") final Long id) {
        certificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto create(@RequestBody(required = false) final CertificateDto certificateDto) {
        CertificateDto certificate = certificateService.create(certificateDto);
        return hateoas.add(certificate);
    }

    @ResponseBody
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto update(@PathVariable("id") final Long id,
                                 @RequestBody(required = false) final CertificateDto certificateDto) {
        CertificateDto certificate = certificateService.update(certificateDto, id);
        return hateoas.add(certificate);
    }

    @ResponseBody
    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto update(@PathVariable("id") final Long id,
                                 @RequestBody(required = false) final CertificatePriceDto price) {
        CertificateDto certificate = certificateService.updatePrice(price, id);
        return hateoas.add(certificate);
    }
}
