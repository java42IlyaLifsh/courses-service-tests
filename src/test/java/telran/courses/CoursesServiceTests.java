package telran.courses;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import telran.courses.dto.Course;
import telran.courses.service.CoursesService;

@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CoursesServiceTests {
	private static final @NotEmpty String COURSE1 = "course1";
	private static final @NotEmpty String LECTURER1 = "lecturer1";
	private static final @Min(80) @Max(500) int HOURS1 = 100;
	private static final @Min(5000) @Max(20000) int COST1 = 10000;
	private static final @NotNull @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}.*") String DATE1 = "2022-05-05";
	static Course course1 = new Course(COURSE1, LECTURER1, HOURS1, COST1, DATE1);
	static Course course2 = new Course(COURSE1, LECTURER1, HOURS1, COST1, DATE1);
	static int id1;
	static int id2;
	@Autowired
CoursesService coursesService;
	@Test
	@Order(1)
	void addCoursesTest() {
		course1 = coursesService.addCourse(course1);
		assertNotNull(course1.id);
		course2 = coursesService.addCourse(course2);
		assertNotNull(course2.id);
		id1 = course1.id;
		id2 = course2.id;
		assertNotEquals(id1, id2);
	}
	@Test
	@Order(2)
	void getCourseTest() {
		assertEquals(course1, coursesService.getCourse(id1));
		assertEquals(course2, coursesService.getCourse(id2));
	}
	@Test
	@Order(3)
	void updateCourseTest() {
		//TODO
	}
	@Test
	@Order(4)
	void removeCourseTest() {
		//TODO
	}

}
