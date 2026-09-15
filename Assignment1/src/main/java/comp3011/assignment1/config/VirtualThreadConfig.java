package comp3011.assignment1.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*Configures Tomcat to use virtual threads for 
handling HTTP requests*/
@Configuration
public class VirtualThreadConfig {
	
	// Customizer configures Tomcat's protocol handler
    // to use a virtual thread executor.
    @Bean
    public TomcatProtocolHandlerCustomizer<ProtocolHandler> virtualThreadCustomizer() {

    	return protocolHandler -> configureVirtualThreads(protocolHandler);
    }
    
    // Create a new virtual thread for each task and assigns
    // the executor to Tomcat for processing incoming requests.
    private void configureVirtualThreads(ProtocolHandler protocolHandler) {

    	ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        protocolHandler.setExecutor(executor);
    }
}