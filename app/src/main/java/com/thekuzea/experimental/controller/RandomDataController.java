package com.thekuzea.experimental.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/generate-random-data")
public interface RandomDataController {

    @GetMapping(
            value = "/{dataLength}",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    ResponseEntity<String> getRandomData(@PathVariable("dataLength") Integer dataLength);
}
