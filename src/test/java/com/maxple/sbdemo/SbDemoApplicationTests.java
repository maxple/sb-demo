package com.maxple.sbdemo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SbDemoApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CatRepository catRepository;

    @Before
    public void setUp() {
        Stream.of("Felix", "Garfield", "Whiskers").forEach(s -> catRepository.save(new Cat(s)));
    }

    @Test
    @WithMockUser(password = "123456")
    public void test() throws Exception {
        MediaType mediaType = MediaType.parseMediaType("application/hal+json;charset=UTF-8");
        this.mvc
                .perform(MockMvcRequestBuilders.get("/cats"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(mediaType))
                .andExpect(result -> {
                    String s = result.getResponse().getContentAsString();
					Assert.assertEquals("3", s
							.split("totalElements")[1]
							.split(":")[1]
							.trim()
							.split(",")[0]);
                });
    }
}
