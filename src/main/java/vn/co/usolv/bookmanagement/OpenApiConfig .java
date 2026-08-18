package vn.co.usolv.bookmanagement; 

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API QUẢN LÝ SÁCH (BOOK MANAGEMENT APP API)")
                        .version("1.0.0")
                        .description("Tài liệu đặc tả hệ thống REST API phục vụ quản lý sách kết hợp Spring Boot & MyBatis.")
                        .contact(new Contact()
                                .name("Đặng Văn Học")
                                .url("https://github.com")));
    }
}
