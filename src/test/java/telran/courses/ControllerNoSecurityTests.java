package telran.courses;
//ilya-hw61

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.courses.dto.Course;
import telran.courses.service.CoursesService;
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"app.security.enable=false"})
class ControllerNoSecurityTests {
	private static final Integer COURSE_ID = 100000;
	private static final String URL_COURSES = "/courses";
	private static final String URL_COURSES_AND_ID = URL_COURSES + "/" + COURSE_ID;
	private static final String WRONG_VALID_REQUES_BY_ID = URL_COURSES + "/" + 123456789;
	@Autowired
MockMvc mockMvc;
	@MockBean
	CoursesService coursesService;
	static Course course;
	static String wrongCourseJson;
	static String correctCourseJson;
	static String courseJsonResponse;
	static ObjectMapper mapper = new ObjectMapper();

	static {
//		ObjectMapper mapper = new ObjectMapper();
		course = new Course("course","lecturer",80, 5000, "2020-01-01");
		try {
			correctCourseJson = mapper.writeValueAsString(course);
			course.id = COURSE_ID;
			courseJsonResponse = mapper.writeValueAsString(course);
			Course wrongCourse = new Course("course", "lect", 10, 5000, "2020-01-01");
			wrongCourseJson = mapper.writeValueAsString(wrongCourse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private ResultActions badRequestCheck(ResultActions act) throws Exception {
		return act.andDo(print()).andExpect(status().isBadRequest());
	}
	@Test
	
	void addCourse() throws  Exception {
		/*
		when(coursesService.addCourse(any(Course.class))).thenReturn(course);
		assertEquals(courseJsonResponse, mockMvc.perform(post("/courses")
				.contentType("application/json").content(correctCourseJson))
				.andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString());
		mockMvc.perform(post("/courses")
				.contentType("application/json").content(wrongCourseJson))
				.andDo(print()).andExpect(status().isBadRequest());
	*/
		when(coursesService.addCourse(any(Course.class))).thenReturn(course);
		assertEquals(courseJsonResponse, getJsonResponse(postAct(correctCourseJson)));
		// course validation check
		badRequestCheck(postAct(wrongCourseJson));

	}

	private ResultActions postAct(String courseJson) throws Exception {
		return mockMvc.perform(post(URL_COURSES).contentType("application/json").content(courseJson));
	}

	private String getJsonResponse(ResultActions act) throws Exception {
		return act.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	void getCourse() throws Exception {
		when(coursesService.getCourse(anyInt())).thenReturn(course);

		assertEquals(courseJsonResponse, getJsonResponse(mockMvc.perform(get(URL_COURSES_AND_ID))));
		 
		badRequestCheck(mockMvc.perform(get(WRONG_VALID_REQUES_BY_ID)));

	}

	
	@Test
	
	void getCourses() throws Exception {

		Course course2 = new Course("course2", "lecturer2", 82, 5002, "2020-02-02");
		course2.id = COURSE_ID + 2;

		List<Course> checkList = new ArrayList<>();
		checkList.add(course);
		checkList.add(course2);

		String listCorrectJson = mapper.writeValueAsString(checkList);

		when(coursesService.getAllCourses()).thenReturn(checkList);
		assertEquals(listCorrectJson, getJsonResponse(mockMvc.perform(get(URL_COURSES))));

	}
	
	@Test
	void removeCourse() throws Exception {
		when(coursesService.removeCourse(anyInt())).thenReturn(course);
		assertEquals(courseJsonResponse, getJsonResponse(mockMvc.perform(delete(URL_COURSES_AND_ID))));
		badRequestCheck(mockMvc.perform(delete(WRONG_VALID_REQUES_BY_ID)));

	}
	@Test
	void updateCourse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Course courseUpdated = new Course("course3", "lecturer3", 83, 5003, "2023-03-03");
		courseUpdated.id = COURSE_ID;
		String courseUpdJsonRequest = mapper.writeValueAsString(courseUpdated);

		when(coursesService.updateCourse(anyInt(), any(Course.class))).thenReturn(courseUpdated);
		assertEquals(courseUpdJsonRequest, getJsonResponse(updateAction(courseUpdJsonRequest)));
		badRequestCheck(updateAction(wrongCourseJson));

		courseUpdated.id = 555555;
		String wrCourseUpdJsonRequest = mapper.writeValueAsString(courseUpdated);
		badRequestCheck(updateAction(wrCourseUpdJsonRequest));

	}

	private ResultActions updateAction(String jsonRequest) throws Exception {
		return mockMvc.perform(put(URL_COURSES_AND_ID).contentType("application/json").content(jsonRequest));
	}
}

