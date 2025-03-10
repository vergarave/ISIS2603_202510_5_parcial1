package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.RecordService;
import uniandes.dse.examen1.services.StatsService;
import uniandes.dse.examen1.services.StudentService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class, StatsService.class })
public class StatServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StatsService statsService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private String login1;
    private String login2;
    private String codCurso1;
    private String codCurso2;

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException, InvalidRecordException {
        CourseEntity curso1 = factory.manufacturePojo(CourseEntity.class);
        curso1 = courseService.createCourse(curso1);
        codCurso1 = curso1.getCourseCode();

        CourseEntity curso2 = factory.manufacturePojo(CourseEntity.class);
        curso2 = courseService.createCourse(curso2);
        codCurso2 = curso2.getCourseCode();

        StudentEntity st1 = factory.manufacturePojo(StudentEntity.class);
        st1 = studentService.createStudent(st1);
        login1 = st1.getLogin();

        StudentEntity st2 = factory.manufacturePojo(StudentEntity.class);
        st2 = studentService.createStudent(st2);
        login2 = st2.getLogin();

        recordService.createRecord(login1, codCurso1, 4.6, "1");
        recordService.createRecord(login1, codCurso2, 3.4, "2");
        recordService.createRecord(login2, codCurso1, 4.0, "3");
        recordService.createRecord(login2, codCurso2, 3.0, "4");
    }

    @Test
    void testGetStudentAverage() throws InvalidRecordException, RepeatedCourseException, RepeatedStudentException {
        
        assertEquals(4.0, statsService.calculateStudentAverage(login1));
    }

    @Test
    void testGetCourseAverage() throws InvalidRecordException, RepeatedCourseException, RepeatedStudentException {
        
        assertEquals(4.3, statsService.calculateCourseAverage(codCurso1));
    }
}

