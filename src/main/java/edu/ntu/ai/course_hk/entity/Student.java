package edu.ntu.ai.course_hk.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat; // 导入
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Student {
    private Integer id;

    @NotEmpty(message = "学号不能为空")
    @Size(min = 5, max = 20, message = "学号长度必须在5到20之间")
    private String studentNumber;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    private String gender;

    @NotNull(message = "年龄不能为空")
    @PositiveOrZero(message = "年龄不能为负数")
    private Integer age;

    @NotEmpty(message = "专业不能为空")
    private String major;

    @NotEmpty(message = "班级不能为空")
    private String className;

    @NotNull(message = "入学日期不能为空")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // <--- 添加这个注解
    private LocalDate enrollmentDate;

    private String contactInfo;
}