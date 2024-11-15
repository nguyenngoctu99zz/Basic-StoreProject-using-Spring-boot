package com.boostmytool.beststore.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


public class EmployeeDto {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "The name is required")
    private String empname;
    @Max(150)
    @Min(18)
    private int age;
    @NotEmpty(message = "The name is required")
    private String address;


    private Integer jobId;

    private MultipartFile imageFileName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public MultipartFile getImageFileName() {
        return imageFileName;
    }


    public void setImageFileName(MultipartFile imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }
}
