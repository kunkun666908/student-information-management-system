package edu.ntu.ai.course_hk.service;

import edu.ntu.ai.course_hk.entity.Student;
import edu.ntu.ai.course_hk.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public Student getStudentById(Integer id) {
        return studentMapper.findById(id);
    }

    public List<Student> getAllStudents() {
        return studentMapper.findAll();
    }

    public List<Student> findStudentsByConditions(Map<String, Object> params) {
        // 可以在这里处理分页逻辑，例如从params获取分页参数，
        // 然后设置到查询中，或者直接在Mapper XML的SQL中处理
        return studentMapper.findByConditions(params);
    }

    public long countStudentsByConditions(Map<String, Object> params) {
        return studentMapper.countByConditions(params);
    }

    public boolean addStudent(Student student) {
        return studentMapper.insert(student) > 0;
    }

    public boolean updateStudent(Student student) {
        return studentMapper.update(student) > 0;
    }

    public boolean deleteStudent(Integer id) {
        return studentMapper.deleteById(id) > 0;
    }
}