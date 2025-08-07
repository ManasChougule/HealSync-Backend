package com.java.loginReg.business.abstracts;

import com.java.loginReg.entities.Ambulance;
import com.java.loginReg.entities.AmbulanceBooking;
import com.java.loginReg.entities.AmbulanceBookingDto;

import java.util.List;

public interface AmbulanceBookingService {

    Ambulance addAmbulance(Ambulance ambulance);

    List<Ambulance> getAvailableAmbulances();

    List<AmbulanceBooking> getAllBookings();

    String bookAmbulance(AmbulanceBookingDto bookingDto);

    // other existing methods like bookAmbulance, getAllBookings, etc.
}
