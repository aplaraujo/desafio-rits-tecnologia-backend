package io.github.aplaraujo.controllers;

import io.github.aplaraujo.dto.AuthResponseDTO;
import io.github.aplaraujo.entities.AuthRequest;
import io.github.aplaraujo.entities.Client;
import io.github.aplaraujo.services.JwtService;
import io.github.aplaraujo.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserDetailsServiceImpl service;
    private final JwtService jwtService;
    private final AuthenticationManager manager;

    @PostMapping("/user")
    public String addNewClient(@RequestBody Client client) {
        return service.addUser(client);
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponseDTO> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtService.generateToken(authRequest.getEmail());

        return ResponseEntity.ok(new AuthResponseDTO(token, "Bearer", 30L * 60L));

    }
}
