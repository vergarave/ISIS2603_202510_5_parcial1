package uniandes.dse.examen1.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public Double calculateStudentAverage(String login) {
        Optional<StudentEntity> student = studentRepository.findByLogin(login);
        List<RecordEntity> records =student.get().getRecords();
        double sum = 0;
        double prom = 0;
        for (int i = 0; i<records.size(); i++){
            RecordEntity rec = records.get(i);
            double grade = rec.getFinalGrade();
            sum += grade;
        }
        prom = sum/records.size();
        return prom;

    }

    public Double calculateCourseAverage(String courseCode) {
        Optional<CourseEntity> course = courseRepository.findByCourseCode(courseCode);
        List<StudentEntity> students = course.get().getStudents();
        double sum = 0;
        double prom = 0;
        for (int i = 0; i<students.size(); i++){
            StudentEntity student = students.get(i);
            List<RecordEntity> records = student.getRecords();
            for (int j = 0; j<records.size(); j++){
                RecordEntity rec = records.get(j);
                if (rec.getCourse().getCourseCode().equals(courseCode)){
                    double grade = rec.getFinalGrade();
                    sum += grade;
                }
            }
            
        }

        prom = sum/students.size();
        return prom;
    }

}
