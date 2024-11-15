package com.boostmytool.beststore.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;


public class JobDTO {

    private Integer id;


    @NotEmpty(message = "The name is required")
    private String title;

    @NotEmpty(message = "The name is required")
    private String discription;

    @ManyToMany(mappedBy = "jobs")
    private Set<Employee> employees;


    public JobDTO(Integer id, String title, String discription, Set<Employee> employees) {
        this.id = id;
        this.title = title;
        this.discription = discription;
        this.employees = employees;
    }

    public JobDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
