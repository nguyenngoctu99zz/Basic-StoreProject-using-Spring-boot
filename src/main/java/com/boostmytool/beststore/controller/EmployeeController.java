package com.boostmytool.beststore.controller;


import com.boostmytool.beststore.dao.JobRepository;
import com.boostmytool.beststore.model.Employee;
import com.boostmytool.beststore.model.EmployeeDto;
import com.boostmytool.beststore.dao.EmployeeRepository;
import com.boostmytool.beststore.model.Job;
import com.boostmytool.beststore.model.JobDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private JobRepository jobRepository;


    @GetMapping({"", "/"})
    public String showProductList(Model model,
                                  @RequestParam(value = "search", required = false) String search,
                                  @RequestParam(value = "sort", defaultValue = "asc") String sort,
                                  @RequestParam(value = "page", defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 5);
        if ("desc".equals(sort)) {
            pageable = PageRequest.of(page, 5, Sort.by("empname").descending());
        } else {
            pageable = PageRequest.of(page, 5, Sort.by("empname").ascending());
        }
        Page<Employee> employeePage;

        if (search != null && !search.isEmpty()) {
            employeePage = repo.findByNameContaining(search, pageable);
        } else {
            employeePage = repo.findAll(pageable);
        }

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort); // Thêm sort vào model

        return "index";
    }




    @GetMapping("/create")
    public String showCreatePage(Model model) {
        EmployeeDto employeeDto = new EmployeeDto();
        model.addAttribute("employeeDto", employeeDto);

        List<Job> jobs = jobRepository.findAll(Sort.by(Sort.Direction.DESC, "id")); // Lấy danh sách công việc
        model.addAttribute("jobs", jobs); // Thêm danh sách công việc vào model
        return "CreateProduct";
    }


    @PostMapping("/create")
    public String createEmployee(
            @Valid @ModelAttribute EmployeeDto employeeDto,
            BindingResult result
    ) {

        // Kiểm tra xem có file hình ảnh không
        if (employeeDto.getImageFileName() == null || employeeDto.getImageFileName().isEmpty()) {
            result.addError(new FieldError("employeeDto", "imageFileName", "The image file is required"));
        }

        if (result.hasErrors()) {
            return "CreateEmployee";
        }

        // Lưu file hình ảnh
        MultipartFile image = employeeDto.getImageFileName();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        // Lưu thông tin nhân viên
        Employee employee = new Employee();
        employee.setId(employeeDto.getId());
        employee.setEmpname(employeeDto.getEmpname());
        employee.setAge(employeeDto.getAge());
        employee.setAddress(employeeDto.getAddress());
        employee.setCreateAt(createdAt);
        employee.setImageFileName(storageFileName);


        Job job = jobRepository.findById(employeeDto.getJobId()).orElse(null);
        if (job != null) {
            Set<Job> jobs = new HashSet<>();
            jobs.add(job);
            employee.setJobs(jobs); // Gán công việc cho nhân viên
        }

        repo.save(employee);

        return "redirect:/employee";
    }



    @GetMapping("/edit")
    public String showEditPage(
            Model model,
            @RequestParam int id
    ) {
        try {
            Employee employee = repo.findById(id).get();
            model.addAttribute("employee", employee);

            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setId(employeeDto.getId());
            employeeDto.setEmpname(employeeDto.getEmpname());
            employeeDto.setAge(employeeDto.getAge());
            employeeDto.setAddress(employeeDto.getAddress());

            model.addAttribute("employeeDto", employeeDto);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/employee";
        }
        return "EditProduct";
    }


    @PostMapping("/edit")
    public String UpdateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute EmployeeDto employeeDto,
            BindingResult result
    ) {
        ///conect database
        try {

            Employee employee = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
            model.addAttribute("employee", employee);


            if (result.hasErrors()) {
                return "EditProduct";
            }

            if(!employeeDto.getImageFileName().isEmpty()){
                // delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + employee.getImageFileName());


                try {
                    Files.delete(oldImagePath);
                }
                catch (Exception ex){
                    System.out.println("Error at: " + ex.getMessage());
                }

                /// save new image
                MultipartFile image = employeeDto.getImageFileName();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName)
                    , StandardCopyOption.REPLACE_EXISTING);

                }
                employee.setImageFileName(storageFileName);
            }


            employee.setId(employeeDto.getId());
            employee.setEmpname(employeeDto.getEmpname());
            employee.setAge(employeeDto.getAge());
            employee.setAddress(employeeDto.getAddress());

            repo.save(employee);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/employee";
    }


    @GetMapping("/delete")
    public String deleteProduct(
            @RequestParam int id
    ) {
        try {
            Employee employee = repo.findById(id).get();

            repo.delete(employee);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/employee";
    }
    @GetMapping("/detail")
    public String showEmployeeDetail(Model model, @RequestParam int id) {
        try {
            Employee employee = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
            model.addAttribute("employee", employee);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/employee";
        }
        return "Detail";
    }

        @GetMapping("/job")
    public String showJobList(Model model) {

        List<Job> jobs = jobRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("jobs", jobs);
        return "JobList";
    }



}
