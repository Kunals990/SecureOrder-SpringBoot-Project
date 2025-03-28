package com.kunals990.secureorder.rest.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "user") @NotBlank String username,
        @Schema(example = "user") @NotBlank String password) {
}
