package edu.ntu.ai.course_hk.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
//import java.util.Date;
import java.time.LocalDate;

@Data
public class Score {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private BigDecimal grade;
    private String semester;

    @NotNull(message = "考试日期不能为空") // 如果是必填项
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // <--- 添加或确认这个注解
    private LocalDate examDate;
    // private LocalDate examDate;

    // 为了方便在查询结果中直接显示学生和课程信息，可以添加这两个对象
    // 但在执行插入/更新时，通常只操作 studentId 和 courseId
    private Student student;
    private Course course;
}