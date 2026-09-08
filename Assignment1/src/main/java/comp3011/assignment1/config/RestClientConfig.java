package comp3011.assignment1.config;

import org.springframework.web.client.RestClient;
import org.springframework.context.annotation.*;

@Configuration
public class RestClientConfig {
	@Bean
	public RestClient RestClientConfig() {
		return RestClient.builder().baseUrl("https://api.openai.com").build();
								
						
	}
}
