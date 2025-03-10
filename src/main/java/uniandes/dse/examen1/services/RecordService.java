package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public RecordEntity createRecord(String loginStudent, String courseCode, Double grade, String semester)
            throws InvalidRecordException {
                Optional<StudentEntity> student = studentRepository.findByLogin(loginStudent);
                if (student.isEmpty()) {
                    throw new InvalidRecordException("el estudiante no existe");
                }
                Optional<CourseEntity> course = courseRepository.findByCourseCode(courseCode);
                if(course.isEmpty()){
                    throw new InvalidRecordException("El curso no existe");
                }
                if(grade<1.5){
                    throw new InvalidRecordException("la nota debe ser mayor a 1.5");
                }
                else if (grade>5){
                    throw new InvalidRecordException("la nota debe ser menor a 5");
                }

                for (RecordEntity record: recordRepository.findAll()){
                    if (record.getStudent().getLogin().equals(loginStudent) && record.getCourse().getCourseCode().equals(courseCode)){
                        if (record.getFinalGrade() >= 3.0){
                            throw new InvalidRecordException("El estudiante ya aprobo el curso");                }
                    }
                }
                

            RecordEntity rec = new RecordEntity();
            rec.setFinalGrade(grade);
            rec.setSemester(semester);
            rec.setStudent(student.get());
            rec.setCourse(course.get());

        
        return recordRepository.save(rec);
    }
}
