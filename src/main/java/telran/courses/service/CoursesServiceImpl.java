package telran.courses.service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.*;
import org.springframework.stereotype.Service;

import telran.courses.dto.Course;
@Service
public class CoursesServiceImpl implements CoursesService{

	
	static Logger LOG = LoggerFactory.getLogger(CoursesService.class);
		
		private static final int MIN_ID = 100000;
		private static final int MAX_ID = 999999;
		
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

	}


