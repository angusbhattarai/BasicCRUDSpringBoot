package com.springboot.thymeleaf.controller;

import com.springboot.thymeleaf.model.Employee;
import com.springboot.thymeleaf.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

 @Autowired
    private EmployeeService employeeService;

 // Display list of employees
    @GetMapping("/")
    public String viewHomePage(Model model) {
        return findPaginated(1,"firstName","asc",model);
    }
    @GetMapping("/showNewEmployeeForm")

    // request came from index.html(showNewEmployeeForm)
    public String showNewEmployeeForm(Model model) {
        // create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    @PostMapping("/saveEmployee")
    public  String saveEmployee(@ModelAttribute("employee") Employee employee) {
        // save employee to database
        employeeService.saveEmployee(employee);
        // if you use return redirect it will go the homepage--- @GetMapping ("/")
        return "redirect:/";

    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
        // get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id) {

        // call delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/";

    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam( name = "sortField", defaultValue = "name") String sortField,
                                @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                Model model) {
        int pageSize = 5;

        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize,sortField,sortDir);
        List<Employee> listEmployees = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listEmployees", listEmployees);
        return "index";
    }
}


