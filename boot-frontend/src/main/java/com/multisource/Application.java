package com.multisource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.multisource.controllers.LandingPageController;

@SpringBootApplication(scanBasePackages = "com.multisource")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
