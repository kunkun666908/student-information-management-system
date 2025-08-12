package edu.ntu.ai.course_hk.mapper;

import edu.ntu.ai.course_hk.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface CourseMapper {
    Course findById(Integer id);
    List<Course> findAll(); // 查询所有课程
    List<Course> findByConditions(Map<String, Object> params); // 多条件查询
    int insert(Course course);
    int update(Course course);
    int deleteById(Integer id);
    long countByConditions(Map<String, Object> params); // 用于分页的总数统计
}