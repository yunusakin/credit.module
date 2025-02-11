package com.yunusakin.credit.module.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Credit Module API Documentation")
                        .description("API for managing customers, loans, and loan installments")
                        .version("1.0.0"))
                .addTagsItem(new Tag().name("Customer APIs").description("Operations related to customers"))
                .addTagsItem(new Tag().name("Loan APIs").description("Operations related to loans"))
                .addTagsItem(new Tag().name("Loan Installment APIs").description("Operations related to loan installments"));
    }
}

