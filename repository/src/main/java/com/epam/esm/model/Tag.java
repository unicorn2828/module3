package com.epam.esm.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tag")
public class Tag extends BaseModel {

    @Column(name = "tag_name")
    private String tagName;

    @ManyToMany(mappedBy = "tags", targetEntity = Certificate.class)
    List<Certificate> certificates;
}
