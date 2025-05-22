package com.umc.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "HealthCheck 관련 API", description = "HealthCheck 관련 Controller")
public class HealthController {

    @Operation(summary = "HealthCheck API (jwt 없이도 가능)", description = "서버가 살아있는 지 확인합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
    })
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
