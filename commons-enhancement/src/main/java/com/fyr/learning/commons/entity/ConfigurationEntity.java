package com.fyr.learning.commons.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tb_sys_configuration")
public class ConfigurationEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name")
    private String key;
    @Column(name = "value")
    private String value;
    @Column(name = "is_activated")
    private String isActivated;
    @Column(name = "comments")
    private String comments;
    @Column(name = "create_date")
    private Date createDate;

}
