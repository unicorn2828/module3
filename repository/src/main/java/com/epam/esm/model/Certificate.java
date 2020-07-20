package com.epam.esm.model;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "certificate")
@EntityListeners(AuditingEntityListener.class)
public class Certificate extends BaseModel<Certificate> {
    private String certificateName;
    private String description;
    private BigDecimal price;
    private LocalDate creationDate;
    @LastModifiedDate
    private LocalDate modificationDate;
    private int duration;

    @ManyToMany(mappedBy = "certificates", targetEntity = Order.class)
    private List<Order> orders;

    @ManyToMany(targetEntity = Tag.class, cascade = CascadeType.DETACH)
    @JoinTable(name = "tag_certificate",
               joinColumns = @JoinColumn(name = "certificate_id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
}
