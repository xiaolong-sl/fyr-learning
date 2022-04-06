package com.fyr.activiti.learning.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "sys_role")
public class SysRoleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<SysUserEntity> users;

    @ManyToMany
    @JoinTable(name = "sys_role_menu",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
    private Set<SysMenuEntity> menus;

    @ManyToMany
    @JoinTable(name = "sys_dept_role",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "dept_id", referencedColumnName = "id")})
    private Set<SysDeptEntity> depts;

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

    public Set<SysUserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<SysUserEntity> users) {
        this.users = users;
    }

    public Set<SysMenuEntity> getMenus() {
        return menus;
    }

    public void setMenus(Set<SysMenuEntity> menus) {
        this.menus = menus;
    }

    public Set<SysDeptEntity> getDepts() {
        return depts;
    }

    public void setDepts(Set<SysDeptEntity> depts) {
        this.depts = depts;
    }
}
