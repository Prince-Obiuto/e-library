package ng.edu.futo.sict_e_library.user_service.controller;

import lombok.RequiredArgsConstructor;
import ng.edu.futo.sict_e_library.user_service.dto.response.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users/health")
@RequiredArgsConstructor
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> healthCheck() {
        Map<String,String> health = new HashMap<>();
        health.put("service", "user-service");
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(
                ApiResponseDTO.success("User Service is running",health)
        );
    }
}
