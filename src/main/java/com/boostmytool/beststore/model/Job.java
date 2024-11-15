package com.boostmytool.beststore.model;


import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String discription;

    @ManyToMany(mappedBy = "jobs")
    private Set<Employee> employees;


    public Job(Integer id, String title, String discription, Set<Employee> employees) {
        this.id = id;
        this.title = title;
        this.discription = discription;
        this.employees = employees;
    }

    public Job() {

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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    /// k có toString sẽ trả về mảng
    @Override
    public String toString() {
        return title;
    }
}
