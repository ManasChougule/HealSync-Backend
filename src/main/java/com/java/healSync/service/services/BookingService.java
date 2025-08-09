package com.java.healSync.service.services;

import com.java.healSync.entity.Ambulance;
import com.java.healSync.entity.BookingRequest;

import java.util.List;

public interface BookingService {
    void bookAmbulance(BookingRequest request);
    List<Ambulance> getAvailableAmbulancesByType(String type);
}









