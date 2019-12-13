package com.scit.xml.controller;

import com.scit.xml.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Value("${schema.review}")
    private String reviewXsd;
}
