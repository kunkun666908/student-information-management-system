package edu.ntu.ai.course_hk.service;

import edu.ntu.ai.course_hk.entity.Score;
import edu.ntu.ai.course_hk.mapper.ScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    public Score getScoreById(Integer id) {
        return scoreMapper.findById(id);
    }

    public Score getScoreByStudentAndCourse(Integer studentId, Integer courseId) {
        return scoreMapper.findByStudentIdAndCourseId(studentId, courseId);
    }

    public List<Score> getScoresByStudentId(Integer studentId) {
        return scoreMapper.findScoresByStudentId(studentId);
    }

    public List<Score> getScoresByCourseId(Integer courseId) {
        return scoreMapper.findScoresByCourseId(courseId);
    }

    public List<Score> findScoresByConditions(Map<String, Object> params) {
        return scoreMapper.findScoresByConditions(params);
    }

    public long countScoresByConditions(Map<String, Object> params) {
        return scoreMapper.countScoresByConditions(params);
    }

    public boolean addScore(Score score) {
        // 检查是否已存在该学生该课程的成绩记录
        Integer existing = scoreMapper.checkIfScoreExists(score.getStudentId(), score.getCourseId());
        if (existing != null && existing > 0) {
            // 可以选择抛出异常，或者返回false，或者更新现有成绩
            System.out.println("Score already exists for studentId: " + score.getStudentId() + ", courseId: " + score.getCourseId());
            return false; // 或者抛出自定义异常
        }
        return scoreMapper.insert(score) > 0;
    }

    public boolean updateScore(Score score) {
        // 如果你的update是基于主键id的
        // return scoreMapper.update(score) > 0;

        // 如果你想根据 studentId 和 courseId 更新，需要调整Mapper的update方法
        // 并且确保score对象中studentId和courseId是有效的
        Score existingScore = scoreMapper.findByStudentIdAndCourseId(score.getStudentId(), score.getCourseId());
        if (existingScore != null) {
            score.setId(existingScore.getId()); // 确保使用正确的记录ID进行更新
            return scoreMapper.update(score) > 0;
        }
        return false; // 记录不存在，无法更新
    }

    public boolean deleteScore(Integer id) {
        return scoreMapper.deleteById(id) > 0;
    }

    public boolean deleteScoreByStudentAndCourse(Integer studentId, Integer courseId) {
        return scoreMapper.deleteByStudentIdAndCourseId(studentId, courseId) > 0;
    }
}