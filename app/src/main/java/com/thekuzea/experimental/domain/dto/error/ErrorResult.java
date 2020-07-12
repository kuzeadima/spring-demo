package com.thekuzea.experimental.domain.dto.error;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor
public class ErrorResult {

    List<ValidationError> errors = new ArrayList<>();

    public void addError(final String source, final String details) {
        this.errors.add(new ValidationError(source, details));
    }
}
