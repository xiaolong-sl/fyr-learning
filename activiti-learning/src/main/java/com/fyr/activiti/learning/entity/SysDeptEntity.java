package com.fyr.activiti.learning.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "sys_dept")
public class SysDeptEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * 部门名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 父级部门ID
     */
    @Column(name = "pid")
    private Integer pid;

    /**
     * 组对应的角色
     */
    @ManyToMany(mappedBy = "depts")
    private Set<SysRoleEntity> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Set<SysRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRoleEntity> roles) {
        this.roles = roles;
    }
}
