package com.java.loginReg.business.concretes;

import com.java.loginReg.business.abstracts.AmbulanceBookingService;
import com.java.loginReg.dataAccess.AmbulanceBookingDao;
import com.java.loginReg.dataAccess.AmbulanceDao;
import com.java.loginReg.dataAccess.HospitalDao;
import com.java.loginReg.entities.Ambulance;
import com.java.loginReg.entities.AmbulanceBooking;
import com.java.loginReg.entities.AmbulanceBookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AmbulanceBookingManager implements AmbulanceBookingService {

    @Autowired
    private AmbulanceBookingDao ambulanceBookingDao;

    @Autowired
    private AmbulanceDao ambulanceDao;

    @Autowired
    private HospitalDao hospitalDao;

    @Override
    public Ambulance addAmbulance(Ambulance ambulance) {
        ambulance.setStatus("available"); // default status
        return ambulanceDao.save(ambulance);
    }

    @Override
    public List<Ambulance> getAvailableAmbulances() {
        return ambulanceDao.findByStatus("available");
    }

    @Override
    public String bookAmbulance(AmbulanceBookingDto bookingDto) {
        Optional<Ambulance> optionalAmbulance = ambulanceDao.findById(bookingDto.getAmbulanceId());

        if (optionalAmbulance.isPresent()) {
            Ambulance ambulance = optionalAmbulance.get();

            // ✅ Check if already booked
            if ("unavailable".equalsIgnoreCase(ambulance.getStatus())) {
                return "Ambulance already booked!";
            }

            // ✅ Save booking
            AmbulanceBooking booking = new AmbulanceBooking();
            booking.setPatientName(bookingDto.getPatientName());
            booking.setAge(bookingDto.getAge());
            booking.setPickupLocation(bookingDto.getPickupLocation());
            booking.setAmbulance(ambulance);
            booking.setHospital(hospitalDao.findById(bookingDto.getHospitalId()).orElse(null));
            booking.setBookingTime(LocalDateTime.now());
            ambulanceBookingDao.save(booking);

            // ✅ Mark ambulance as unavailable
            ambulance.setStatus("unavailable");
            ambulanceDao.save(ambulance);

            return "Ambulance booked successfully!";
        }

        return "Ambulance not found.";
    }

    @Override
    public List<AmbulanceBooking> getAllBookings() {
        return ambulanceBookingDao.findAll();
    }
}
