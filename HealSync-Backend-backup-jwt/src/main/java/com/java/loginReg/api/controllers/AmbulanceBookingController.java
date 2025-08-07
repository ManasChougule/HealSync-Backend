package com.java.loginReg.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.java.loginReg.business.abstracts.AmbulanceBookingService;
import com.java.loginReg.entities.AmbulanceBookingDto;

@RestController
@RequestMapping("/api/ambulance-bookings") // ✅ fixed this path
@CrossOrigin(origins = "http://localhost:3000")
public class AmbulanceBookingController {

    @Autowired
    private AmbulanceBookingService bookingService;

    @PostMapping("/book")
    public String bookAmbulance(@RequestBody AmbulanceBookingDto bookingDto) {
        return bookingService.bookAmbulance(bookingDto);
    }
}

