package telran.courses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.courses.dto.Course;
import telran.courses.entities.CourseEntity;
import telran.courses.exceptions.BadRequestException;
import telran.courses.exceptions.ResourceNotFoundException;
import telran.courses.repo.CourseRepository;
@Service
public class CoursesServiceImpl extends AbstractCoursesService {
@Autowired
	CourseRepository courseRepository;
	@Override
	@Transactional
	public Course addCourse(Course course) {
		int id = getId();
		course.id = id;
		CourseEntity courseEntity = CourseEntity.build(course);
		courseRepository.save(courseEntity);
		return course;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Course> getAllCourses() {
		
		return courseRepository.findAll().stream().map(CourseEntity::getCourseDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Course getCourse(int id) {
		CourseEntity course = courseRepository.findById(id).orElse(null);
		if (course == null) {
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		return course.getCourseDto();
	}

	@Override
	@Transactional
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
	@Transactional
	public Course updateCourse(int id, Course course) {
		if (!exists(id)) {
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		if (id != course.id) {
			throw new BadRequestException(String.format("Id mismatching: received id - %d; course id - %d",
					id, course.id));
		}
		CourseEntity courseEntity = courseRepository.getById(id);
		CourseEntity.fillEntity(course, courseEntity);
		
		
		
		return courseEntity.getCourseDto();
	}

	@Override
	protected boolean exists(int id) {
		
		return courseRepository.existsById(id);
	}

}
