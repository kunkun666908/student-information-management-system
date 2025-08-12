package edu.ntu.ai.course_hk.service;

import edu.ntu.ai.course_hk.entity.Course;
import edu.ntu.ai.course_hk.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    public Course getCourseById(Integer id) {
        return courseMapper.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseMapper.findAll();
    }

    public List<Course> findCoursesByConditions(Map<String, Object> params) {
        return courseMapper.findByConditions(params);
    }

    public long countCoursesByConditions(Map<String, Object> params) {
        return courseMapper.countByConditions(params);
    }

    public boolean addCourse(Course course) {
        return courseMapper.insert(course) > 0;
    }

    public boolean updateCourse(Course course) {
        return courseMapper.update(course) > 0;
    }

    public boolean deleteCourse(Integer id) {
        return courseMapper.deleteById(id) > 0;
    }
}