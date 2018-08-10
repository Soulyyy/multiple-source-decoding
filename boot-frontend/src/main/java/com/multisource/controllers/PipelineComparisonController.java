package com.multisource.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.multisource.data.Matrix;
import com.multisource.data.MatrixImpl;

@RestController
public class PipelineComparisonController {

  @RequestMapping("/pipeline")
  public String compare(@RequestParam(value = "input", defaultValue = "10101010") String input,
                        @RequestParam(value = "matrix", defaultValue = "777-76.mat") String matrixFileName) {

    return new MatrixImpl(new Integer[][]{new Integer[]{0}, new Integer[]{0}}).toString();
  }
}
