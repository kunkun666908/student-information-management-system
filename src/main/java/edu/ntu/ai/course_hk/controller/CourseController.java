package edu.ntu.ai.course_hk.controller;

import edu.ntu.ai.course_hk.entity.Course;
import edu.ntu.ai.course_hk.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid; // 如果要用校验
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listCourses(@RequestParam(required = false) String courseNumber,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String teacherName,
                              @RequestParam(required = false) String departmentOffering,
                              Model model) {
        Map<String, Object> params = new HashMap<>();
        params.put("courseNumber", courseNumber);
        params.put("name", name);
        params.put("teacherName", teacherName);
        params.put("departmentOffering", departmentOffering);

        List<Course> courses = courseService.findCoursesByConditions(params);
        model.addAttribute("courses", courses);
        model.addAttribute("searchParams", params);
        return "course/list"; // 指向 templates/course/list.html
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/form"; // 指向 templates/course/form.html
    }

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute("course") Course course, // 假设使用了@Valid
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("Validation/Binding Errors during ADD Course:");
            result.getAllErrors().forEach(error -> {
                String errorMessage = error.getObjectName() + " - " + error.getDefaultMessage();
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errorMessage += " (Field: " + fieldError.getField() + ", Rejected Value: '" + fieldError.getRejectedValue() + "')";
                }
                System.out.println(errorMessage);
            });
            return "course/form";
        }
        courseService.addCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return "redirect:/courses";
        }
        model.addAttribute("course", course);
        return "course/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Integer id,
                               @Valid @ModelAttribute("course") Course course, // 假设使用了@Valid
                               BindingResult result, Model model) {
        course.setId(id);
        if (result.hasErrors()) {
            System.out.println("Validation/Binding Errors during EDIT Course (ID: " + id + "):");
            result.getAllErrors().forEach(error -> {
                String errorMessage = error.getObjectName() + " - " + error.getDefaultMessage();
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errorMessage += " (Field: " + fieldError.getField() + ", Rejected Value: '" + fieldError.getRejectedValue() + "')";
                }
                System.out.println(errorMessage);
            });
            return "course/form";
        }
        courseService.updateCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}