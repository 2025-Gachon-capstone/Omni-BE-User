package org.example.omnibeuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.omnibeuser.client")
public class OmniBeUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmniBeUserApplication.class, args);
    }

}
