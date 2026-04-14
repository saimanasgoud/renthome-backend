package com.renthome.renthome_backend.controller;

import com.renthome.renthome_backend.config.JwtUtil;
import com.renthome.renthome_backend.entity.Owner;
import com.renthome.renthome_backend.repository.OwnerRepository;
import com.renthome.renthome_backend.service.EmailService; // ✅ FIXED
import com.renthome.renthome_backend.service.MagicLinkService;
import com.renthome.renthome_backend.service.OtpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OwnerRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MagicLinkService magicLinkService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    private OwnerRepository ownerRepository;

    @Value("${admin.mobile}")
private String adminMobile;

@Value("${admin.password}")
private String adminPassword;

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

    String mobile = body.get("mobile");
    String password = body.get("password");

    if (mobile == null || password == null) {
        return ResponseEntity.badRequest().body("Mobile & Password required");
    }

    // ================= ADMIN LOGIN (USING MOBILE FIELD) =================
if (mobile.equals(adminMobile) && password.equals(adminPassword)) {
        String token = jwtUtil.generateToken(0L);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "ownerId", 0,
                "role", "ADMIN"
        ));
    }

    // ================= USER / OWNER LOGIN =================
    Owner owner = ownerRepository.findByMobile(mobile);

    if (owner == null) {
        return ResponseEntity.status(401).body("Mobile number not registered");
    }

    if (!owner.getPassword().equals(password)) {
        return ResponseEntity.status(401).body("Incorrect password");
    }

    String token = jwtUtil.generateToken(owner.getId());

    return ResponseEntity.ok(Map.of(
            "token", token,
            "ownerId", owner.getId(),
            "role", owner.getRole() != null ? owner.getRole() : "USER"
    ));
}

    @PostMapping("/send-magic-link")
    public ResponseEntity<?> sendMagicLink(@RequestBody Map<String, String> request) {

        try {
            String email = request.get("email");
            System.out.println("STEP 1: Request received for email: " + email);

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Email is required"));
            }

            Owner owner = repo.findTopByEmail(email);

            if (owner == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "❌ Email not registered"));
            }

            String token = magicLinkService.generateToken(email);
            System.out.println("STEP 2: Token generated");

            String link = frontendUrl + "/magic-login?token=" + token;
            System.out.println("STEP 3: Link created: " + link);

            System.out.println("STEP 4: Calling email service...");

            new Thread(() -> {
                emailService.sendMagicLink(email, link, owner.getName());
            }).start();
            System.out.println("STEP 5: Email service completed");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "✅ Magic link sent to " + email));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "❌ Email failed: " + e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {

        try {
            // ✅ extract token
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing token");
            }

            String token = authHeader.replace("Bearer ", "");

            // ✅ get ownerId from JWT
            Long ownerId = jwtUtil.extractOwnerId(token);

            // ✅ fetch owner from DB
            Owner owner = repo.findById(ownerId).orElse(null);

            if (owner == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            // ✅ return clean data
            return ResponseEntity.ok(Map.of(
                    "name", owner.getName(),
                    "mobile", owner.getMobile(),
                    "email", owner.getEmail()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching profile");
        }
    }

    @PostMapping("/magic-login")
    public ResponseEntity<?> magicLogin(@RequestParam String token) {

        System.out.println("TOKEN RECEIVED FROM FRONTEND: " + token);
        String email = magicLinkService.validateToken(token);

        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid or expired link");
        }

        // Owner owner = repo.findByEmail(email);
        // Owner owner = repo.getOwnerByEmail(email);
        System.out.println("STEP 1");

        Owner owner = repo.getOwnerByEmail(email);

        System.out.println("STEP 2: " + owner);

        if (owner == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String jwt = jwtUtil.generateToken(owner.getId());

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "ownerId", owner.getId(),
                "role", owner.getRole()));
    }

    // ================= SEND OTP =================

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        System.out.println("EMAIL RECEIVED: " + email);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        Owner owner = repo.findTopByEmail(email);

        if (owner == null) {
            System.out.println("OWNER NOT FOUND");
            return ResponseEntity.badRequest().body("Email not registered");
        }

        System.out.println("OWNER FOUND: " + owner.getEmail());

        String otp = otpService.generateOtp(email);
        System.out.println("OTP: " + otp);

        return ResponseEntity.ok("OTP sent successfully");
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        ownerRepository.deleteById(id);
        return "User deleted successfully";
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String otp = body.get("otp");

        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity.status(401).body("Invalid OTP");
        }

        Owner owner = repo.findTopByEmail(email);

        if (owner == null) {
            return ResponseEntity.badRequest().body("Email not registered");
        }

        String token = jwtUtil.generateToken(owner.getId());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "ownerId", owner.getId(),
                "role", owner.getRole() != null ? owner.getRole() : "USER"));
    }

    // ================= SIGNUP =================

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Owner owner) {

        try {

            if (owner.getMobile() == null || owner.getPassword() == null || owner.getEmail() == null) {
                return ResponseEntity.badRequest().body("Mobile, Email & Password required");
            }

            if (repo.findByMobile(owner.getMobile()) != null) {
                return ResponseEntity.badRequest().body("Mobile already registered");
            }

            if (repo.findTopByEmail(owner.getEmail()) != null) {
                return ResponseEntity.badRequest().body("Email already registered");
            }

            // ✅ FIX
            owner.setRole("OWNER");

            Owner saved = repo.save(owner);

            String token = jwtUtil.generateToken(saved.getId());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "ownerId", saved.getId(),
                    "role", saved.getRole(),
                    "message", "Signup successful"));

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 IMPORTANT
            return ResponseEntity.status(500).body("ERROR: " + e.getMessage());
        }
    }
}