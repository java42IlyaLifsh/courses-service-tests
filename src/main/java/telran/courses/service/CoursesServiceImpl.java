package telran.courses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import telran.courses.dto.Course;
import telran.courses.entities.CourseEntity;
import telran.courses.exceptions.ResourceNotFoundException;
import telran.courses.repo.CourseRepository;

public class CoursesServiceImpl extends AbstractCoursesService {
@Autowired
	CourseRepository courseRepository;
	@Override
	public Course addCourse(Course course) {
		int id = getId();
		CourseEntity courseEntity = CourseEntity.build(course);
		courseRepository.save(courseEntity);
		return course;
	}

	@Override
	public List<Course> getAllCourses() {
		
		return courseRepository.findAll().stream().map(CourseEntity::getCourseDto).toList();
	}

	@Override
	public Course getCourse(int id) {
		CourseEntity course = courseRepository.findById(id).orElse(null);
		if (course == null) {
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		return course.getCourseDto();
	}

	@Override
	public Course removeCourse(int id) {
		
		Course res = getRemovedUpdated(id);
		courseRepository.deleteById(id);
		return res;
	}
	private Course getRemovedUpdated(int id) {
		if (!exists(id)) {
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		CourseEntity courseEntity = courseRepository.getById(id);
		Course res = courseEntity.getCourseDto();
		return res;
	}

	@Override
	public Course updateCourse(int id, Course course) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean exists(int id) {
		
		return courseRepository.existsById(id);
	}

}
