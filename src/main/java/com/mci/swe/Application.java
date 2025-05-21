package com.mci.swe;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication(
  excludeName = {
    "org.springframework.boot.autoconfigure.http.client.HttpClientAutoConfiguration",
    "org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration"
  }
)
@Theme("default")
public class Application implements AppShellConfigurator {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone(); // You can also use Clock.systemUTC()
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
