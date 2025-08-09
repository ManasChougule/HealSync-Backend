package com.java.healSync.controller;

import com.java.healSync.service.services.BookingService;
import com.java.healSync.entity.Ambulance;
import com.java.healSync.entity.BookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private BookingService bookingService;

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

