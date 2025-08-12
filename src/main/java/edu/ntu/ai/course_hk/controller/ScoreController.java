package edu.ntu.ai.course_hk.controller;

import edu.ntu.ai.course_hk.entity.Course;
import edu.ntu.ai.course_hk.entity.Score;
import edu.ntu.ai.course_hk.entity.Student;
import edu.ntu.ai.course_hk.service.CourseService;
import edu.ntu.ai.course_hk.service.ScoreService;
import edu.ntu.ai.course_hk.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StudentService studentService; // 用于获取学生列表供选择

    @Autowired
    private CourseService courseService;   // 用于获取课程列表供选择

    // 显示成绩列表和查询页面
    @GetMapping
    public String listScores(@RequestParam(required = false) String studentName,
                             @RequestParam(required = false) String studentNumber,
                             @RequestParam(required = false) String courseName,
                             @RequestParam(required = false) String courseNumber,
                             @RequestParam(required = false) String semester,
                             Model model) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentName", studentName);
        params.put("studentNumber", studentNumber);
        params.put("courseName", courseName);
        params.put("courseNumber", courseNumber);
        params.put("semester", semester);

        List<Score> scores = scoreService.findScoresByConditions(params);
        model.addAttribute("scores", scores);
        model.addAttribute("searchParams", params);
        return "score/list"; // 指向 templates/score/list.html
    }

    // 显示录入/修改成绩的表单
    // GET /scores/manage?studentId=1&courseId=1 (用于修改已存在的)
    // GET /scores/manage (用于选择学生和课程进行录入)
    @GetMapping("/manage")
    public String showManageScoreForm(@RequestParam(required = false) Integer studentId,
                                      @RequestParam(required = false) Integer courseId,
                                      Model model) {
        Score score = new Score();
        if (studentId != null && courseId != null) {
            Score existingScore = scoreService.getScoreByStudentAndCourse(studentId, courseId);
            if (existingScore != null) {
                score = existingScore;
            } else {
                // 如果记录不存在，但提供了ID，可以预设studentId和courseId
                score.setStudentId(studentId);
                score.setCourseId(courseId);
            }
        }

        List<Student> students = studentService.getAllStudents(); // 获取所有学生
        List<Course> courses = courseService.getAllCourses();   // 获取所有课程

        model.addAttribute("score", score);
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        return "score/form"; // 指向 templates/score/form.html
    }

    // 处理成绩的录入或修改
    @PostMapping("/manage")
    public String saveOrUpdateScore(@Valid @ModelAttribute("score") Score score,
                                    BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        List<Student> students = studentService.getAllStudents();
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("students", students); // 确保返回表单时下拉列表数据存在
        model.addAttribute("courses", courses);   // 同上

        if (result.hasErrors()) {
            System.out.println("Validation/Binding Errors for Score:");
            result.getAllErrors().forEach(error -> {
                String errorMessage = error.getObjectName() + " - " + error.getDefaultMessage();
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errorMessage += " (Field: " + fieldError.getField() + ", Rejected Value: '" + fieldError.getRejectedValue() + "')";
                }
                System.out.println(errorMessage);
            });
            // score 对象已由 @ModelAttribute 添加回模型
            return "score/form";
        }

        boolean success;
        String message;
        // 判断是新增还是更新
        // 如果 score.id 不为 null 且大于0，或者通过studentId和courseId能找到记录，则为更新
        Score existingScore = scoreService.getScoreByStudentAndCourse(score.getStudentId(), score.getCourseId());

        if (existingScore != null) { // 更新逻辑
            score.setId(existingScore.getId()); // 确保使用正确的ID进行更新
            success = scoreService.updateScore(score);
            message = success ? "成绩更新成功！" : "成绩更新失败或记录不存在。";
        } else { // 新增逻辑
            success = scoreService.addScore(score);
            message = success ? "成绩录入成功！" : "成绩录入失败，可能该学生已选此课程。";
        }

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/scores";
        } else {
            model.addAttribute("errorMessage", message);
            // score 对象已由 @ModelAttribute 添加回模型
            return "score/form";
        }
    }


    // 查询某个学生的所有课程成绩
    @GetMapping("/student/{studentId}")
    public String listScoresByStudent(@PathVariable Integer studentId, Model model) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            // 处理学生不存在的情况
            return "redirect:/students"; // 或者一个错误页面
        }
        List<Score> scores = scoreService.getScoresByStudentId(studentId);
        model.addAttribute("student", student);
        model.addAttribute("scores", scores);
        return "score/student_scores"; // 指向 templates/score/student_scores.html
    }

    // 查询某门课程的所有学生成绩
    @GetMapping("/course/{courseId}")
    public String listScoresByCourse(@PathVariable Integer courseId, Model model) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            // 处理课程不存在的情况
            return "redirect:/courses"; // 或者一个错误页面
        }
        List<Score> scores = scoreService.getScoresByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("scores", scores);
        return "score/course_scores"; // 指向 templates/score/course_scores.html
    }


    // (可选) 删除成绩记录的端点
    // 通常从列表页触发，可以基于score.id或studentId+courseId
    @GetMapping("/delete")
    public String deleteScore(@RequestParam Integer studentId, @RequestParam Integer courseId, RedirectAttributes redirectAttributes) {
        boolean success = scoreService.deleteScoreByStudentAndCourse(studentId, courseId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "成绩删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "成绩删除失败！");
        }
        return "redirect:/scores"; // 或重定向到之前的页面
    }

}