package me.qlibin;

import me.qlibin.entity.Book;
import me.qlibin.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CookbookApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BookRepository repository;

	@Before
	public void setupMockMvc() {
	}


	@Test
	public void contextLoads() {
		assertEquals(1, repository.count());
	}

	@LocalServerPort
	int port;

	@Test
	public void webappBookIsbnApi() throws IOException {
//		Book book = restTemplate.getForObject("http://localhost:" + port + "/books/978-1-78528-415-1", Book.class);
//		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//		headers.setAccept(Arrays.asList(MediaType.parseMediaType("application/hal+json;charset=UTF-8")));
//		HttpEntity<Book> entity = new HttpEntity<>(headers);
//		ResponseEntity<Book> bookResponse =
//				restTemplate.exchange(
//						URI.create("http://localhost:" + port + "/books/978-1-78528-415-1"),
//						HttpMethod.GET, entity, Book.class);
//		Book book = bookResponse.getBody();
		Book book = restTemplate.getForObject("/books/978-1-78528-415-1", Book.class);
//		String book = restTemplate.getForObject("/books/978-1-78528-415-1", String.class);
		assertNotNull(book);
		assertEquals("Packt", book.getPublisher().getName());
	}

	@Test
	public void webappPublisherApi() throws Exception {
		mockMvc.perform(get("/publishers/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType("application/hal+json")))
				.andExpect(content().string(containsString("Packt")))
				.andExpect(jsonPath("$.name").value("Packt"));
	}


}
