package com.fyr.activiti.learning.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "sys_menu")
public class SysMenuEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * 资源编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 资源名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 菜单/按钮URL
     */
    @Column(name = "url")
    private String url;

    /**
     * 资源类型（1:菜单，2:按钮）
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 父级菜单ID
     */
    @Column(name = "pid")
    private Integer pid;

    /**
     * 排序号
     */
    @Column(name = "sort")
    private Integer sort;

    @ManyToMany(mappedBy = "menus")
    private Set<SysRoleEntity> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Set<SysRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRoleEntity> roles) {
        this.roles = roles;
    }
}
