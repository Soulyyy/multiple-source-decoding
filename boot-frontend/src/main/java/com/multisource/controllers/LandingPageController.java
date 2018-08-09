package com.multisource.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingPageController {

  @RequestMapping("/xd")
  public String greeting() {
    return "töötab!";
  }
}
