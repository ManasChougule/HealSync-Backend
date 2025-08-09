package com.java.healSync.api.controllers;

import com.java.healSync.business.concretes.BookingService;
import com.java.healSync.entities.Ambulance;
import com.java.healSync.entities.BookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<String> bookAmbulance(@RequestBody BookingRequest request) {
        bookingService.bookAmbulance(request);
        return ResponseEntity.ok("Ambulance booked successfully");
    }

    @GetMapping("/available")
    public List<Ambulance> getAvailableAmbulancesByType(@RequestParam("type") String type) {
        return bookingService.getAvailableAmbulancesByType(type);
    }
}

