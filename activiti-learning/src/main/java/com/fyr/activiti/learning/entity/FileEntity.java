package com.fyr.activiti.learning.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tb_upload_file")
public class FileEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "original_name")
    private String originalName;
    @Column(name = "type")
    private String type;
    @Column(name = "status")
    private String status;
    @Column(name = "uploader")
    private String uploader;
    @Column(name = "upload_date")
    private Date uploadDate;
}
