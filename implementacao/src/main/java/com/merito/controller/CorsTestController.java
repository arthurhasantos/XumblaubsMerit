package com.merito.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cors-test")
@CrossOrigin(origins = "*")
public class CorsTestController {
    
    @GetMapping
    public String testCors() {
        return "CORS funcionando!";
    }
    
    @PostMapping
    public String testCorsPost(@RequestBody String data) {
        return "CORS POST funcionando! Dados: " + data;
    }
}
