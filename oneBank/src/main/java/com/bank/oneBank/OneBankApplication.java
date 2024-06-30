package com.bank.oneBank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "One Bank App",
				description = "The One Bank REST APIs Documentation",
				version = "v1.0",
				contact=@Contact(
					name="Karthik B M",
					email="karthikbm0808@gmail.com",
					url="https://github.com/karthikbm44/onebank-app"
				),
				license = @License(
						name = "The One Bank",
						url = "https://github.com/karthikbm44/onebank-app"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The One App Documentation",
				url = "https://github.com/karthikbm44/onebank-app"
		)
)
public class OneBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneBankApplication.class, args);
	}

}
