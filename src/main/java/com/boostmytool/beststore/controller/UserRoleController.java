package com.boostmytool.beststore.controller;

import com.boostmytool.beststore.dao.EmployeeRepository;
import com.boostmytool.beststore.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/userHome")
public class UserRoleController {

    @Autowired
    private EmployeeRepository repo;

//    @GetMapping
//    public String showUserHome(Model model, @RequestParam(value = "search", required = false) String search) {
//        /// Model la mot interface dung de chuyen du lievieu tu controller den w
//        /// Interface này cung cấp các phương thức để thêm dữ liệu vào model, sau đó dữ liệu này sẽ được truyền đến view để hiển thị cho người dùng.
//        List<Employee> employee;
//        if (search != null && !search.isEmpty()) {
//            employee = repo.findByNameContaining(search);
//
//        } else {
//            employee = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
//        }
//
//
//        model.addAttribute("employee", employee);
//        // Ở đây, chúng ta đang thêm một thuộc tính vào model với tên là "products" và giá trị là danh sách các sản phẩm lấy được từ cơ sở dữ liệu.
//        // Điều này cho phép dữ liệu này được truyền từ Controller đến View để hiển thị cho người dùng.
//        model.addAttribute("search", search); // Thêm giá trị search vào model để sử dụng trong view
//        return "IndexUserRole";
//    }

    @GetMapping({"", "/"})
    public String showProductList(Model model,
                                  @RequestParam(value = "search", required = false) String search,
                                  @RequestParam(value = "page", defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 5); // Chỉ hiển thị 5 bản ghi mỗi trang
        Page<Employee> employeePage;

        if (search != null && !search.isEmpty()) {
            employeePage = repo.findByNameContaining(search, pageable);
        } else {
            employeePage = repo.findAll(pageable);
        }

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("search", search);
        return "IndexUserRole";
    }

}