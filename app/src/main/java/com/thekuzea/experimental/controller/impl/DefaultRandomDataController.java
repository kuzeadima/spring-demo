package com.thekuzea.experimental.controller.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.controller.RandomDataController;

@RestController
public class DefaultRandomDataController implements RandomDataController {

    @Override
    public ResponseEntity<String> getRandomData(final Integer dataLength) {
        if(dataLength < (Integer.MAX_VALUE / 1000)) {
            return ResponseEntity.ok(RandomStringUtils.randomAlphabetic(dataLength));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
