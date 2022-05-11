package telran.courses;
//ilya-hw61
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.courses.dto.Course;
import telran.courses.dto.LoginData;
import telran.courses.dto.LoginResponse;
import telran.courses.security.Account;
import telran.courses.security.AccountingManagement;
import telran.courses.service.CoursesService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityTests {
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
	
	static String userToken="xxx";
	static String adminToken;
	ObjectMapper mapper = new ObjectMapper();
	@BeforeEach
	void setUp() {
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(accounting.getAccount(adminEmail)).thenReturn(adminAccount);
		when(accounting.getAccount(userEmail)).thenReturn(userAccount);
	}
	@Test
	@Order(1)
	void loginTest() throws  Exception {
		
		LoginData loginDataAdmin = new LoginData();
		loginDataAdmin.email = adminEmail;
		loginDataAdmin.password = "xxxxxxxxxxxx";
		LoginData loginDataUser = new LoginData();
		loginDataUser.email = userEmail;
		loginDataUser.password = "xxxxxxxxxxxx";
		LoginResponse loginResponseAdmin;
		LoginResponse loginResponseUser;
		String loginResponseJson = mockMvc.perform(post("/login").contentType("application/json")
				.content(mapper.writeValueAsString(loginDataAdmin)))
		.andReturn().getResponse().getContentAsString();
		loginResponseAdmin = mapper.readValue(loginResponseJson, LoginResponse.class);
		adminToken = loginResponseAdmin.accessToken;
		//TODO the same for loginResponseUser but with userAccount
		
		
		
	}
	@Test
	@Order(2)
	void addCourse() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/courses").contentType("application/json")
				.content(mapper.writeValueAsString(new Course(userEmail, adminToken, 0, 0, adminEmail)))
				.header("Authorization", adminToken)).andDo(print()).andExpect(status().isBadRequest());
	}
	
}
