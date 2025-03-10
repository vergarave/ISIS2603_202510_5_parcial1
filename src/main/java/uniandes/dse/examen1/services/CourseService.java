package uniandes.dse.examen1.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.repositories.CourseRepository;

@Slf4j
@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;


    @Transactional
    public CourseEntity createCourse(CourseEntity newCourse) throws RepeatedCourseException {
        if (!courseRepository.findByCourseCode(newCourse.getCourseCode()).isEmpty()){
            throw new RepeatedCourseException("no puede haber curso con codigo repetido");
        }
        
        return courseRepository.save(newCourse);
    }
}
