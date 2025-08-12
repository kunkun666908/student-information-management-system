package edu.ntu.ai.course_hk.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Course {
    private Integer id;
    private String courseNumber;
    private String name;
    private BigDecimal credits;
    private String teacherName;
    private String departmentOffering;
    private String description;
}