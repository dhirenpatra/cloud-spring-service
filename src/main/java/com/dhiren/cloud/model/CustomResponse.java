package com.dhiren.cloud.model;

import java.time.Instant;
import java.util.Map;

public record CustomResponse (
        Map<String, String> errors,
        Instant instant
) {


}
