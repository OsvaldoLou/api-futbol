package api_futbol.com.api_futbol.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;



@Configuration
public class SpringDock {

    //http://localhost:8080/api-futbol/swagger-ui/index.html
		@Bean
		GroupedOpenApi swagger() {
			return GroupedOpenApi.builder()
					.group("api_futbol.com.api_futbol")
					.packagesToScan("api_futbol.com.api_futbol.controllers")
					.build();
		}
		
	    @Bean
	    OpenAPI openAPI() {
	       
	        Contact contact = new Contact() //
	                .name("Desenvolvedores") //
	                .url("");
	       
	        Info info = new Info() //
	                .title("API do projeto AGFP") //
	                .version("0.0.1") //
	                .contact(contact) //
	                .description("API responsável pelo funcionamento do aplicativo AGFP.");
	       
	        return new OpenAPI() //
	                .info(info); //
	    }


}
