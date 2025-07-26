package com.java.loginReg.api.controllers;

import java.util.List;

import com.java.loginReg.business.abstracts.AmbulanceBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.java.loginReg.entities.Ambulance;

@RestController
@RequestMapping("/api/ambulances")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AmbulanceController {

    @Autowired
    private AmbulanceBookingService ambulanceService;

    @PostMapping("/add")
    public Ambulance addAmbulance(@RequestBody Ambulance ambulance) {
        return ambulanceService.addAmbulance(ambulance);
    }

    @GetMapping("/available")
    public List<Ambulance> getAvailableAmbulances() {
        return ambulanceService.getAvailableAmbulances();
    }
}

