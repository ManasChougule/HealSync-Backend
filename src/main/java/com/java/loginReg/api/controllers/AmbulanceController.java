package com.java.loginReg.api.controllers;

import com.java.loginReg.business.abstracts.AmbulanceService;
import com.java.loginReg.entities.Ambulance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulances")
@CrossOrigin(origins = "http://localhost:3000")
public class AmbulanceController {

    @Autowired
    private AmbulanceService ambulanceService;

    @PostMapping("/add")
    public Ambulance addAmbulance(@RequestBody Ambulance ambulance) {
        return ambulanceService.addAmbulance(ambulance);
    }

    @GetMapping("/available")
    public List<Ambulance> getAvailableAmbulances() {
        return ambulanceService.getAvailableAmbulances();
    }

    @GetMapping("/available/{type}")
    public List<Ambulance> getAvailableAmbulancesByType(@PathVariable String type) {
        return ambulanceService.getAvailableAmbulancesByType(type);
    }

    @PostMapping("/book/{id}")
    public String bookAmbulance(@PathVariable Long id) {
        return ambulanceService.bookAmbulance(id);
    }
}
