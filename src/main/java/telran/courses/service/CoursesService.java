package telran.courses.service;

import java.util.List;

import telran.courses.dto.Course;

public interface CoursesService {
	Course addCourse(Course course);
	
	List<Course> getAllCourses();

	Course getCourse(int id);

	Course removeCourse(int id);

	Course updateCourse(int id, Course course);
	
	
}
