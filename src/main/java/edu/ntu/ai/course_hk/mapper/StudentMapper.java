package edu.ntu.ai.course_hk.mapper;

import edu.ntu.ai.course_hk.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map; // 用于多条件查询

@Mapper
public interface StudentMapper {
    Student findById(Integer id);
    List<Student> findAll(); // 基础查询所有
    // 多条件查询示例，参数用Map传递
    List<Student> findByConditions(Map<String, Object> params);
    int insert(Student student);
    int update(Student student);
    int deleteById(Integer id);
    long countByConditions(Map<String, Object> params); // 用于分页统计总数
}