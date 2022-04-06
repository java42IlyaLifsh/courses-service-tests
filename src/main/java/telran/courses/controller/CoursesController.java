package telran.courses.controller;

import java.util.List;

import javax.validation.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import telran.courses.dto.Course;
import telran.courses.service.CoursesService;
@RestController
@RequestMapping("/courses")
@Validated

public class CoursesController {
	static Logger LOG = LoggerFactory.getLogger(CoursesController.class);
	@Autowired
	private CoursesService coursesService;

	@PostMapping
	
	Course addCourse(@RequestBody @Valid Course course) {
		Course courseAdded = coursesService.addCourse(course);
		LOG.debug("added course with id {}", courseAdded.id);
		return courseAdded;
	}

	@GetMapping
	List<Course> getCourses() {
		
		List<Course> courses = coursesService.getAllCourses();
		LOG.trace("getting {} courses", courses.size());
		return courses;
	}

}
