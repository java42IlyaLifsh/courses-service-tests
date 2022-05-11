package telran.courses;
//ilyaL-hw61
   
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.courses.dto.Course;
import telran.courses.dto.LoginData;
import telran.courses.dto.LoginResponse;
import telran.courses.exceptions.ResourceNotFoundException;
import telran.courses.security.Account;
import telran.courses.security.AccountingManagement;
import telran.courses.service.CoursesService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	CoursesService coursesService;
	@MockBean
	PasswordEncoder passwordEncoder;
	@MockBean
	AccountingManagement accounting;
	String adminEmail = "admin@tel-ran.com";
	String userEmail = "user@tel-ran.com";
	Account userAccount = new Account(adminEmail, "user", "USER");
	Account adminAccount = new Account(userEmail, "admin", "ADMIN");
	Course course = new Course("course", "lectirer", 80, 5000, "2020-02-02");
	static String userToken;
	static String adminToken;	
	private static String HEADER_AUTO = "Authorization";
	String extraStr="&end";

	ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		course.id = 1234567;
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(accounting.getAccount(adminEmail)).thenReturn(adminAccount);
		when(accounting.getAccount(userEmail)).thenReturn(userAccount);
	}

	@Test
	@Order(1)
	void loginTest() throws Exception {

		LoginData loginDataAdmin = new LoginData();
		loginDataAdmin.email = adminEmail;
		loginDataAdmin.password = "xxxxxxxxxxxx";
		LoginData loginDataUser = new LoginData();
		loginDataUser.email = userEmail;
		loginDataUser.password = "xxxxxxxxxxxx";
		LoginResponse loginResponseAdmin;
		LoginResponse loginResponseUser;

		String loginResponseJsonAdmin = getLogRespJson(loginDataAdmin);
		loginResponseAdmin = mapper.readValue(loginResponseJsonAdmin, LoginResponse.class);
		adminToken = loginResponseAdmin.accessToken;
		String loginResponseJsonUser = getLogRespJson(loginDataUser);
		loginResponseUser = mapper.readValue(loginResponseJsonUser, LoginResponse.class);
		userToken = loginResponseUser.accessToken;

	}

	private String getLogRespJson(LoginData loginData)
			throws UnsupportedEncodingException, Exception, JsonProcessingException {
		return mockMvc
				.perform(post("/login").contentType("application/json").content(mapper.writeValueAsString(loginData)))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	@Order(2)
	void addCourse() throws Exception {
		Course wrongDataCourse = new Course(userEmail, adminToken, 0, 0, adminEmail);
		
		// 400 wrong data for adding by admin
		postAction(adminToken, wrongDataCourse).andExpect(status().isBadRequest());

		// 403 response for userToken
		postAction(userToken, course).andExpect(status().isForbidden());

		// 403 response for wrong Token
		postAction(adminToken + extraStr, course).andExpect(status().isForbidden());

	}

	private ResultActions postAction(String token, Course courseForPost) throws Exception {
		return mockMvc.perform(post("/courses").contentType("application/json")
				.content(mapper.writeValueAsString(courseForPost)).header(HEADER_AUTO, token)).andDo(print());
	}

	@Test
	@Order(3)
	void removeCourse() throws Exception {
		when(coursesService.removeCourse(anyInt())).thenThrow(ResourceNotFoundException.class);
		
		// 404 for admin if id not exists

		delAction(adminToken).andExpect(status().isNotFound());

		// 403 response for userToken
		delAction(userToken).andExpect(status().isForbidden());

		// 403 response for wrong Token
		delAction(adminToken + extraStr).andExpect(status().isForbidden());
	}

	private ResultActions delAction(String token) throws Exception {
		return mockMvc.perform(delete("/courses/999998").header(HEADER_AUTO, token)).andDo(print());
	}

	@Test
	@Order(4)
	void getCourse() throws Exception {
		when(coursesService.getCourse(anyInt())).thenThrow(ResourceNotFoundException.class);
		//404 for admin if id not exists
		getAction(adminToken).andExpect(status().isNotFound());
		
		// 404 for user if id did'n exists
		getAction(userToken).andExpect(status().isNotFound());

		// 403 response for wrong Token
		getAction(adminToken + extraStr).andExpect(status().isForbidden());
	}
	private ResultActions getAction(String token) throws Exception {
		return mockMvc.perform(get("/courses/123456").header(HEADER_AUTO, token)).andDo(print());
	}
	
	@Test
	@Order(5)
	void updateCourse() throws Exception {

		when(coursesService.updateCourse(anyInt(), any(Course.class))).thenThrow(ResourceNotFoundException.class);
		// 404 for admin if id not exist
		putAction(adminToken).andExpect(status().isNotFound());
		
		// 403 response for user Token
		putAction(userToken).andExpect(status().isForbidden());
		
		// 403 response for wrong Token
		putAction(adminToken + extraStr).andExpect(status().isForbidden());


	}
	private ResultActions putAction(String token) throws Exception {
		return mockMvc.perform(put("/courses/1234567").contentType("application/json")
				.content(mapper.writeValueAsString(course)).header(HEADER_AUTO, token)).andDo(print());
	}
}