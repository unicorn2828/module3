package com.epam.esm.model;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order extends BaseModel {
    private BigDecimal orderPrice;
    private String ownerName;
    private LocalDate creationDate;
    @ManyToMany(targetEntity = Certificate.class, cascade = CascadeType.DETACH)
    @JoinTable(name = "order_certificate",
               joinColumns = @JoinColumn(name = "order_id"),
               inverseJoinColumns = @JoinColumn(name = "certificate_id"))
    private List<Certificate> certificates;
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
