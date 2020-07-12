package com.thekuzea.experimental.domain.dto.error;

import lombok.Value;

@Value
public class ValidationError {

    String source;

    String details;
}
