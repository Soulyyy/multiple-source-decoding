package com.multisource.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.multisource.controllers.LandingPageController;

@SpringBootApplication
@ComponentScan(basePackageClasses = LandingPageController.class)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
