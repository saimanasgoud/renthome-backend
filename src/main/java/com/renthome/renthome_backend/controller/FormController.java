package com.renthome.renthome_backend.controller;

import com.renthome.renthome_backend.config.JwtUtil;
import com.renthome.renthome_backend.entity.FormData;
import com.renthome.renthome_backend.repository.FormDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forms")
@CrossOrigin(origins = "*")
public class FormController {

    @Autowired
    private FormDataRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    /*
     * =====================================================
     * SEARCH
     * =====================================================
     */
   @GetMapping("/search")
public List<FormData> searchHomes(
        @RequestParam(required = false) String pincode,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String area
) {

    return repo.findAll()
            .stream()
            .filter(FormData::isPosted)

            // ✅ PINCODE FILTER
            .filter(h -> pincode == null || 
                (h.getPincode() != null && h.getPincode().equals(pincode)))

            // ✅ CITY FILTER
            .filter(h -> city == null || 
                (h.getCity() != null && h.getCity().equalsIgnoreCase(city)))

            // ✅ STATE FILTER
            .filter(h -> state == null || 
                (h.getState() != null && h.getState().equalsIgnoreCase(state)))

            // ✅ AREA FILTER (from JSON)
            .filter(h -> {
                if (area == null) return true;

                try {
                    String json = h.getFormJson();
                    if (json == null) return false;

                    return json.toLowerCase().contains(area.toLowerCase());
                } catch (Exception e) {
                    return false;
                }
            })

            .toList();
}
    /*
     * =====================================================
     * SAVE PROPERTY (JWT)
     * =====================================================
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveProperty(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody FormData data) {

        // ✅ TOKEN CHECK
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid token");
        }

        String token = authHeader.replace("Bearer ", "");
        Long ownerId = jwtUtil.extractOwnerId(token);

        data.setOwnerId(ownerId);

        data.setRole("OWNER");

        if (data.getFormJson() == null || data.getFormJson().isEmpty()) {
            return ResponseEntity.badRequest().body("formJson is missing");
        }

        if (data.getPropertyType() == null || data.getPropertyType().isEmpty()) {
            return ResponseEntity.badRequest().body("propertyType is missing");
        }

        data.setPosted(false);

        if (data.getTitle() == null) {
            data.setTitle(data.getPropertyType() + " Property");
        }

        // ✅ DEBUG (optional)
        System.out.println("LAT: " + data.getLatitude());
        System.out.println("LNG: " + data.getLongitude());

        // ❗ Optional fallback (if frontend doesn't send)
       if (data.getLatitude() == null || data.getLongitude() == null) {
    return ResponseEntity.badRequest().body("Latitude & Longitude required");
}

if (data.getCity() == null || data.getCity().isEmpty()) {
    return ResponseEntity.badRequest().body("City is required");
}

if (data.getState() == null || data.getState().isEmpty()) {
    return ResponseEntity.badRequest().body("State is required");
}

if (data.getPincode() == null || data.getPincode().isEmpty()) {
    return ResponseEntity.badRequest().body("Pincode is required");
}
        return ResponseEntity.status(201).body(repo.save(data));
    }

    /*
     * =====================================================
     * GET ALL
     * =====================================================
     */
    
 @GetMapping("/search/filter")
public List<FormData> filterHomes(
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String pincode,
        @RequestParam(required = false) String bhk,
        @RequestParam(required = false) String propertyType,
        @RequestParam(required = false) String maxRent
) {

    List<FormData> homes = repo.findAll();

    return homes.stream()
            .filter(FormData::isPosted)

            // ✅ CITY FILTER
            .filter(h -> city == null || 
                (h.getCity() != null && h.getCity().equalsIgnoreCase(city)))

            // ✅ PINCODE FILTER
            .filter(h -> pincode == null || 
                (h.getPincode() != null && h.getPincode().equals(pincode)))

            // ✅ BHK FILTER (FROM JSON OR FIELD)
            .filter(h -> bhk == null || 
                (h.getBhk() != null && h.getBhk().contains(bhk)))

            // ✅ PROPERTY TYPE
            .filter(h -> propertyType == null || 
                h.getPropertyType().equalsIgnoreCase(propertyType))

            // ✅ RENT FILTER
            .filter(h -> {
                if (maxRent == null || h.getRent() == null) return true;
                return Integer.parseInt(h.getRent()) <= Integer.parseInt(maxRent);
            })

            .toList();
}

    @GetMapping("/all")
    public List<FormData> getAllProperties() {
        return repo.findAll()
                .stream()
                .filter(FormData::isPosted) // ONLY LIVE PROPERTIES
                .toList();
    }

    /*
     * =====================================================
     * GET BY ID
     * =====================================================
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable Long id) {

        FormData property = repo.findById(id).orElse(null);

        if (property == null) {
            return ResponseEntity.status(404).body("Property not found");
        }

        return ResponseEntity.ok(property);
    }

    /*
     * =====================================================
     * UPDATE PROPERTY (JWT + SECURITY)
     * =====================================================
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id,
            @RequestBody FormData updated) {
        System.out.println("AUTH HEADER: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid token");
        }

        String token = authHeader.replace("Bearer ", "");

        Long ownerId;
        try {
            ownerId = jwtUtil.extractOwnerId(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        FormData existing = repo.findById(id).orElse(null);

        if (existing == null) {
            return ResponseEntity.status(404).body("Property not found");
        }

        if (existing.getOwnerId() == null || !existing.getOwnerId().equals(ownerId)) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        existing.setFormJson(updated.getFormJson());
        existing.setPropertyType(updated.getPropertyType());

        return ResponseEntity.ok(repo.save(existing));
    }

    /*
     * =====================================================
     * POST PROPERTY
     * =====================================================
     */
    @PutMapping("/post/{id}")
    public ResponseEntity<?> postProperty(@PathVariable Long id) {

        FormData property = repo.findById(id).orElse(null);

        if (property == null) {
            return ResponseEntity.status(404).body("Property not found");
        }

        property.setPosted(true);

        if (property.getGeneratedAt() == null) {
            property.setGeneratedAt(LocalDate.now());
        }

        repo.save(property);

        return ResponseEntity.ok("Property posted successfully");
    }

    /*
     * =====================================================
     * GET BY OWNER ID
     * =====================================================
     */
    @GetMapping("/owner")
    public ResponseEntity<?> getMyProperties(
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing token");
        }

        String token = authHeader.replace("Bearer ", "");
        Long ownerId = jwtUtil.extractOwnerId(token);

        List<FormData> properties = repo.findByOwnerId(ownerId);

        return ResponseEntity.ok(properties);
    }

    /*
     * =====================================================
     * DELETE
     * =====================================================
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {

        if (!repo.existsById(id)) {
            return ResponseEntity.status(404).body("Property not found");
        }

        repo.deleteById(id);

        return ResponseEntity.ok("Deleted successfully");
    }
}