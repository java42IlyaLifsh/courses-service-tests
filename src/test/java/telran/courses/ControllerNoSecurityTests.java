package telran.courses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.courses.dto.Course;
import telran.courses.service.CoursesService;
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"app.security.enable=false"})
class ControllerNoSecurityTests {
	private static final Integer COURSE_ID = 100000;
	@Autowired
MockMvc mockMvc;
	@MockBean
	CoursesService coursesService;
	static Course course;
	static String wrongCourseJson;
	static String correctCourseJson;
	static String courseJsonResponse;
	static {
		ObjectMapper mapper = new ObjectMapper();
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
	@Test
	void addCourse() throws  Exception {
		when(coursesService.addCourse(any(Course.class))).thenReturn(course);
		assertEquals(courseJsonResponse, mockMvc.perform(post("/courses")
				.contentType("application/json").content(correctCourseJson))
				.andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString());
		mockMvc.perform(post("/courses")
				.contentType("application/json").content(wrongCourseJson))
				.andDo(print()).andExpect(status().isBadRequest());
				
		
	}

}
