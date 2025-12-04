package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.secruity.CurrentUser;
import com.cntt.rentalmanagement.secruity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo(@CurrentUser UserPrincipal userPrincipal, 
                                                HttpServletRequest request) {
        // Lấy token từ header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", userPrincipal.getId());
        response.put("email", userPrincipal.getEmail());
        response.put("username", userPrincipal.getUsername());
        response.put("roles", userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        response.put("authorities_count", userPrincipal.getAuthorities().size());
        response.put("token_received", token != null ? "Yes (length: " + token.length() + ")" : "No");
        
        System.out.println("\n========================================");
        System.out.println("=== DEBUG USER INFO ===");
        System.out.println("========================================");
        System.out.println("User ID: " + userPrincipal.getId());
        System.out.println("Email: " + userPrincipal.getEmail());
        System.out.println("Username: " + userPrincipal.getUsername());
        System.out.println("Roles Count: " + userPrincipal.getAuthorities().size());
        System.out.println("Roles Detail: " + userPrincipal.getAuthorities());
        System.out.println("Roles List: " + userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")));
        if (token != null) {
            System.out.println("Token (first 50 chars): " + token.substring(0, Math.min(50, token.length())) + "...");
        }
        System.out.println("========================================\n");
        
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only")
    public ResponseEntity<?> adminOnly(@CurrentUser UserPrincipal userPrincipal) {
        System.out.println("✅ ADMIN endpoint accessed by: " + userPrincipal.getEmail());
        return ResponseEntity.ok(Map.of(
            "message", "Welcome Admin!", 
            "role", "ADMIN",
            "user", userPrincipal.getEmail(),
            "roles", userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        ));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user-only")
    public ResponseEntity<?> userOnly(@CurrentUser UserPrincipal userPrincipal) {
        System.out.println("✅ USER endpoint accessed by: " + userPrincipal.getEmail());
        return ResponseEntity.ok(Map.of(
            "message", "Welcome User!", 
            "role", "USER",
            "user", userPrincipal.getEmail(),
            "roles", userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        ));
    }

    @PreAuthorize("hasRole('RENTALER')")
    @GetMapping("/rentaler-only")
    public ResponseEntity<?> rentalerOnly(@CurrentUser UserPrincipal userPrincipal) {
        System.out.println("✅ RENTALER endpoint accessed by: " + userPrincipal.getEmail());
        return ResponseEntity.ok(Map.of(
            "message", "Welcome Rentaler!", 
            "role", "RENTALER",
            "user", userPrincipal.getEmail(),
            "roles", userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        ));
    }
    
    @GetMapping("/check-roles")
    public ResponseEntity<?> checkRoles(@CurrentUser UserPrincipal userPrincipal) {
        Map<String, Object> roleCheck = new HashMap<>();
        roleCheck.put("user_id", userPrincipal.getId());
        roleCheck.put("email", userPrincipal.getEmail());
        roleCheck.put("total_roles", userPrincipal.getAuthorities().size());
        
        Map<String, Boolean> hasRoles = new HashMap<>();
        hasRoles.put("ROLE_ADMIN", userPrincipal.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        hasRoles.put("ROLE_USER", userPrincipal.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        hasRoles.put("ROLE_RENTALER", userPrincipal.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_RENTALER")));
        
        roleCheck.put("has_roles", hasRoles);
        roleCheck.put("all_roles", userPrincipal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        
        System.out.println("\n=== ROLE CHECK ===");
        System.out.println("User: " + userPrincipal.getEmail());
        System.out.println("Has ROLE_ADMIN: " + hasRoles.get("ROLE_ADMIN"));
        System.out.println("Has ROLE_USER: " + hasRoles.get("ROLE_USER"));
        System.out.println("Has ROLE_RENTALER: " + hasRoles.get("ROLE_RENTALER"));
        System.out.println("==================\n");
        
        return ResponseEntity.ok(roleCheck);
    }
}
