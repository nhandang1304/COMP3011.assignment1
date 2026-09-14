package comp3011.assignment1.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.Executor;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VirtualThreadConfig {

    @Bean
    public TomcatProtocolHandlerCustomizer<ProtocolHandler> virtualThreadCustomizer() {

    	return protocolHandler -> configureVirtualThreads(protocolHandler);
    }

    private void configureVirtualThreads(ProtocolHandler protocolHandler) {

    	ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        protocolHandler.setExecutor(executor);
    }
}