package com.fyr.learning.file.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fyr.learning.file.excel.GenderConverter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "employees")
public class EmployeeEntity implements Serializable {
    @Id
    @Column(name = "emp_no")
    @ExcelProperty(value = "员工编号", order = 1)
    private Integer empNo;
    @Column(name = "birth_date")
    @ExcelProperty(value = "出生日期", order = 2)
    private Date birthDate;
    @Column(name = "first_name")
    @ExcelProperty(value = "姓", order = 3)
    private String firstName;
    @Column(name = "last_name")
    @ExcelProperty(value = "名", order = 4)
    private String lastName;
    @Column(name = "gender")
    @ExcelProperty(value = "性别", order = 5, converter = GenderConverter.class)
    private String gender;
    @Column(name = "hire_date")
    @ExcelProperty(value = "入职日期", order = 6)
    private Date hireDate;

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
}
