package telran.courses.service;

import java.util.List;

import telran.courses.dto.Course;

public interface CoursesService {
	Course addCourse(Course course);
	
	List<Course> getAllCourses();
	
}
