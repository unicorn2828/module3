package com.epam.esm.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.modelmapper.ModelMapper;

import java.util.Objects;

public class DtoMapper {
    private ModelMapper mapper = new ModelMapper();

    public Tag toTag(TagDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Tag.class);
    }

    public TagDto toTagDto(Tag entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, TagDto.class);
    }

    public Certificate toCertificate(CertificateDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Certificate.class);
    }

    public CertificateDto toCertificateDto(Certificate entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, CertificateDto.class);
    }
}
