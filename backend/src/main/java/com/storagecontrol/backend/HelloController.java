package com.storagecontrol.backend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello() {
        Map<String, Object> response = Map.of(
            "status", "OK",
            "message", "SAP Storage working properly",
            "timestamp", Instant.now().toString()
        );
        return ResponseEntity.ok(response); //same as ResponseEntity.status(200).body(body);
    }
}