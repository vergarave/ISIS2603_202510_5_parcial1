package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

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
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.StudentService;
import uniandes.dse.examen1.services.RecordService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class })
public class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private String login;
    private String courseCode;

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
        CourseEntity newCourse = factory.manufacturePojo(CourseEntity.class);
        newCourse = courseService.createCourse(newCourse);
        courseCode = newCourse.getCourseCode();

        StudentEntity newStudent = factory.manufacturePojo(StudentEntity.class);
        newStudent = studentService.createStudent(newStudent);
        login = newStudent.getLogin();
    }

    /**
     * Tests the normal creation of a record for a student in a course
          * @throws InvalidRecordException 
          */
         @Test
         void testCreateRecord() throws InvalidRecordException {
        RecordEntity record = recordService.createRecord(login, courseCode, 4.0, "4");
        assertNotNull(record);
        assertEquals(4.0, record.getFinalGrade(), 0.01);
    }

    /**
     * Tests the creation of a record when the login of the student is wrong
     */
    @Test
    void testCreateRecordMissingStudent() {
        assertThrows(InvalidRecordException.class, () -> {
            recordService.createRecord("login random", courseCode, 3.5, "3");
        });
    }

    /**
     * Tests the creation of a record when the course code is wrong
     */
    @Test
    void testCreateInscripcionMissingCourse() {
        assertThrows(InvalidRecordException.class, () -> {
            recordService.createRecord(login, "codigo random", 3.5, "8");
        });
    }

    /**
     * Tests the creation of a record when the grade is not valid
     */
    @Test
    void testCreateInscripcionWrongGrade() {
        assertThrows(InvalidRecordException.class, () -> {
            recordService.createRecord(login, courseCode, 1.3, "1");
        });

        assertThrows(InvalidRecordException.class, () -> {
            recordService.createRecord(login, courseCode, 6.0, "1");
        });
    }

    /**
     * Tests the creation of a record when the student already has a passing grade
     * for the course
          * @throws InvalidRecordException 
          */
        @Test
        void testCreateInscripcionRepetida1() throws InvalidRecordException {
        recordService.createRecord(login, courseCode, 3.5, "2");

        assertThrows(InvalidRecordException.class, () -> {
            recordService.createRecord(login, courseCode, 4.0, "2");
        });
    }

    /**
     * Tests the creation of a record when the student already has a record for the
     * course, but he has not passed the course yet.
          * @throws InvalidRecordException 
          */
        @Test
        void testCreateInscripcionRepetida2() throws InvalidRecordException {
        recordService.createRecord(login, courseCode, 2.5, "4");
        assertDoesNotThrow(() -> {
            recordService.createRecord(login, courseCode, 2.8, "4");
        });
    }
}
