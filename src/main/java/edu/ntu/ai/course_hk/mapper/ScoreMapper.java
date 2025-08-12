package edu.ntu.ai.course_hk.mapper;

import edu.ntu.ai.course_hk.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 用于传递多个参数给XML
import java.util.List;
import java.util.Map;

@Mapper
public interface ScoreMapper {
    // 基础CRUD
    Score findById(Integer id);
    Score findByStudentIdAndCourseId(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);
    int insert(Score score);
    int update(Score score); // 通常是更新成绩
    int deleteById(Integer id);
    int deleteByStudentIdAndCourseId(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);

    // 查询学生所有课程成绩 (需要关联课程表)
    List<Score> findScoresByStudentId(Integer studentId);

    // 查询课程下所有学生成绩 (需要关联学生表)
    List<Score> findScoresByCourseId(Integer courseId);

    // (可选) 多条件查询成绩，可以包含学生姓名、课程名称等进行模糊查询
    List<Score> findScoresByConditions(Map<String, Object> params);
    long countScoresByConditions(Map<String, Object> params); // 用于分页

    // (可选) 检查某个学生是否已选某门课 (用于录入成绩前判断)
    Integer checkIfScoreExists(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);
}