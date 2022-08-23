package com.balinasoft.firsttask.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "image")
@Setter
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String url;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column
    double lat;

    @Column
    double lng;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinTable(
            name = "image_category",
            joinColumns = {@JoinColumn(name = "image_id", table = "image", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "category_id", table = "category", referencedColumnName = "id")
            })
    private Category category;
}
