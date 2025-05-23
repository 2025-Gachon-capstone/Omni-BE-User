package org.example.omnibeuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.omnibeuser.client")
// @EntityScan(basePackages = "org.example.omnibeuser.entity")
// @EnableJpaRepositories(basePackages = "org.example.omnibeuser.repository")
public class OmniBeUserApplication {

    public static void main(String[] args) {
        // SpringApplication.run(OmniBeUserApplication.class, args);
        SpringApplication app = new SpringApplication(OmniBeUserApplication.class);
        app.setApplicationStartup(new BufferingApplicationStartup(2048));
        app.run(args);
    }

}
