package com.mybank.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.mybank.app.controller.AdminController;

//@SpringBootTest
@AutoConfigureMockMvc
class MybankApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private AdminController adminController;

	@Test
	void contextLoads() {
		assertThat(adminController).isNotNull();
	}
//	@Test
//	public void shouldReturnBadRequest() throws Exception {
//		this.mockMvc.perform(post("/v1/admin/addEmployee")).andDo(print()).andExpect(status().isBadRequest());
//	}
	

}
