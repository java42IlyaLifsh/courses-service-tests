package telran.courses.service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import telran.courses.dto.Course;
import telran.courses.exceptions.ResourceNotFoundException;
import static telran.courses.api.ApiConstants.*;
@Service
public class CoursesServiceImpl implements CoursesService{
@Value("${app.interval.minutes: 1}")
	int interval;
	static Logger LOG = LoggerFactory.getLogger(CoursesService.class);
		
		
		
	private Map<Integer, Course> courses = new HashMap<>();
	@Override
	public  Course addCourse(Course course) {
	    course.id = generateId();
	    Course res = add(course);
	   
	    return res;
	}

	private Course add(Course course) {
		courses.put(course.id, course);
		return course;
	}

	

	

	@Override
	public List<Course> getAllCourses() {
	    
	    return new ArrayList<>(courses.values());
	}

	
	private Integer generateId() {
	    ThreadLocalRandom random = ThreadLocalRandom.current();
	    int randomId;

	    do {
	        randomId = random.nextInt(MIN_ID, MAX_ID);
	    } while (exists(randomId));
	    return randomId;
	}

	private boolean exists(int id) {
		return courses.containsKey(id);
	}

	@Override
	public Course getCourse(int id) {
		Course course = courses.get(id);
		if (course == null) {
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		return course;
	}

	@Override
	public Course removeCourse(int id) {
		Course course = courses.remove(id);
		if (course == null){
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		return course;
	}

	@Override
	public Course updateCourse(int id, Course course) {
		
		Course courseUpdated = courses.replace(id, course);
		if (courseUpdated == null){
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		return courseUpdated;
	}

	@Override
	public void restore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
	@PostConstruct
	void restoreInvocation() {
		LOG.debug("interval: {}", interval);
	}

	}


