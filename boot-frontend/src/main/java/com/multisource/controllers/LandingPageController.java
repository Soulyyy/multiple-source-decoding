package com.multisource.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingPageController {

  @RequestMapping("/")
  public String greeting() {
    return "Töötab!";
  }
}
