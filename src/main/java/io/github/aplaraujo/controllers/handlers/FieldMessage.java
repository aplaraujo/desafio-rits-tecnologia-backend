package io.github.aplaraujo.controllers.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldMessage {
    private final String field;
    private final String error;
}
