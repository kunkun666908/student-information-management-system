package edu.ntu.ai.course_hk.controller;

import edu.ntu.ai.course_hk.entity.Student;
import edu.ntu.ai.course_hk.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // 导入 BindingResult
import org.springframework.validation.FieldError;    // 导入 FieldError
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid; // 导入 @Valid
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // (如果你之前添加了 InitBinder，并且 Student 实体类中 enrollmentDate 是 LocalDate, 确保它已被移除或调整)
    /*
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // ... (如果实体类是LocalDate，这个通常不需要了，除非有特殊需求)
    }
    */

    @GetMapping
    public String listStudents(@RequestParam(required = false) String studentNumber,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String gender,
                               @RequestParam(required = false) String major,
                               @RequestParam(required = false) String className,
                               Model model) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentNumber", studentNumber);
        params.put("name", name);
        params.put("gender", gender);
        params.put("major", major);
        params.put("className", className);

        List<Student> students = studentService.findStudentsByConditions(params);
        model.addAttribute("students", students);
        model.addAttribute("searchParams", params);
        return "student/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student()); // 提供一个空的Student对象给表单
        return "student/form";
    }

    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute("student") Student student, // 添加 @Valid 和 ("student")
                             BindingResult result, // 接收BindingResult
                             Model model) { // 添加Model参数，以便返回错误时能传递数据

        if (result.hasErrors()) {
            System.out.println("Validation/Binding Errors during ADD:");
            result.getAllErrors().forEach(error -> {
                String errorMessage = error.getObjectName() + " - " + error.getDefaultMessage();
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errorMessage += " (Field: " + fieldError.getField() + ", Rejected Value: '" + fieldError.getRejectedValue() + "')";
                }
                System.out.println(errorMessage);
            });
            // 不需要手动 model.addAttribute("student", student); 因为 @ModelAttribute("student") 已经做了
            return "student/form"; // 返回表单页面，Thymeleaf会使用result中的错误信息
        }

        try {
            studentService.addStudent(student);
        } catch (Exception e) {
            // 处理可能的Service层或数据库层异常
            System.err.println("Error adding student: " + e.getMessage());
            // 可以向模型添加一个通用错误消息
            model.addAttribute("formError", "添加学生失败，请稍后再试或联系管理员。");
            // 重新填充表单数据（student对象已通过@ModelAttribute存在）
            return "student/form";
        }
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/students"; // 或者可以添加一个错误提示
        }
        model.addAttribute("student", student);
        return "student/form";
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Integer id,
                                @Valid @ModelAttribute("student") Student student, // 添加 @Valid 和 ("student")
                                BindingResult result, // 接收BindingResult
                                Model model) { // 添加Model参数

        student.setId(id); // 确保ID被设置，因为路径变量ID优先

        if (result.hasErrors()) {
            System.out.println("Validation/Binding Errors during EDIT (ID: " + id + "):");
            result.getAllErrors().forEach(error -> {
                String errorMessage = error.getObjectName() + " - " + error.getDefaultMessage();
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errorMessage += " (Field: " + fieldError.getField() + ", Rejected Value: '" + fieldError.getRejectedValue() + "')";
                }
                System.out.println(errorMessage);
            });
            // 不需要手动 model.addAttribute("student", student);
            return "student/form"; // 返回表单页面
        }

        try {
            studentService.updateStudent(student);
        } catch (Exception e) {
            System.err.println("Error updating student: " + e.getMessage());
            model.addAttribute("formError", "更新学生信息失败，请稍后再试或联系管理员。");
            // student 对象已经通过 @ModelAttribute 存在于模型中
            return "student/form";
        }
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    // 你之前添加的测试日期绑定的方法
    @GetMapping("/test-date")
    @ResponseBody
    public String testDateBinding(@RequestParam("myDate")
                                  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate myDate) {
        if (myDate != null) {
            return "Date received: " + myDate.toString();
        } else {
            return "Date is null";
        }
    }
}