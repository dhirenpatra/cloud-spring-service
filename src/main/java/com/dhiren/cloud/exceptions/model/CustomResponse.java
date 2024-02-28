package com.dhiren.cloud.exceptions.model;

import java.time.Instant;
import java.util.Map;

public record CustomResponse (
        Map<String, String> errors,
        Instant instant
) {


}
