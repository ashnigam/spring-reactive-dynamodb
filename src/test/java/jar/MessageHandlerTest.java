package jar;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.is;

public class MessageHandlerTest {
	private WebTestClient testClient;

	@BeforeAll
	void setUp() throws Exception {
		//this.testClient = WebTestClient
		//		.bindToRouterFunction(new MessageHandler().routes()).build();
	}

	@Test
	void testMessage() throws Exception {
        /*
		this.testClient.post().uri("/messages") //
				.syncBody(new Message("Hello")).exchange() //
				.expectStatus().isOk() //
				.expectBody(String.class).isEqualTo("{\"text\":\"Hello\"}");

		this.testClient.get().uri("/messages") //
				.exchange() //
				.expectStatus().isOk() //
                .expectBody(String.class).isEqualTo("[{\"text\":\"Hello\"}]");
                */
	}
}
