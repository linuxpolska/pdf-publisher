package com.example.linux.pdfPublisheru;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.linux.pdfPublisheru.service.StorageService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.stream.Stream;

@AutoConfigureMockMvc
@SpringBootTest
class PdfPublisheruApplicationTests {

//	@Test
//	public void sendPdfFileToPageOfConfluenceSuccess() throws URISyntaxException, IOException {
//		String pdfFilePath = "src/main/resources/sample.pdf";
//		URL url = getClass().getClassLoader().getResource(pdfFilePath);
//		if (url == null) {
//			throw new IOException("File " + pdfFilePath + " not found");
//		}
//		Path path = Paths.get(url.toURI());
//		String fileContent = Files.readString(path);
//		System.out.println(fileContent.getBytes() != null);
//	}

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StorageService storageService;

	@Test
	public void shouldListAllFiles() throws Exception {
		given(this.storageService.loadAll())
				.willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

		this.mvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(model().attribute("files",
						Matchers.contains("http://localhost/files/first.txt",
								"http://localhost/files/second.txt")));
	}

	@Test
	public void shouldSaveUploadedFile() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
				"text/plain", "Spring Framework".getBytes());
		this.mvc.perform(multipart("/").file(multipartFile))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", "/"));

		then(this.storageService).should().store(multipartFile);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should404WhenMissingFile() throws Exception {
		given(this.storageService.loadAsResource("test.txt"))
				.willThrow(FileNotFoundException.class);

		this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
	}

	@Ignore
	@Test
	public void send() throws UnirestException {
//		Unirest.setTimeouts(0, 0);
		HttpResponse<String> response = Unirest.post("http://localhost:8090/rest/api/content/65603/child/attachment")
				.header("X-Atlassian-Token", "nocheck")
				.header("Authorization", "Basic YWRtaW46YWRtaW4=")
				.header("Cookie", "JSESSIONID=D9BBD7D8D62840835A5EEA59DBCC412D")
				.field("file", new File("/D:/Grafiki/SpacesBools04.png"))
				.field("minorEdit", "true")
				.field("comment", "Example attachment comment", "text/plain")
				.asString();
		Assertions.assertEquals(response.getStatus(), HttpStatus.ACCEPTED);
	}
}
