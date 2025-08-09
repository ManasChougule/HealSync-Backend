package com.java.healSync.business.concretes;

import com.java.healSync.dataAccess.AmbulanceRepository;
import com.java.healSync.dataAccess.BookingRepository;
import com.java.healSync.entities.Ambulance;
import com.java.healSync.entities.Booking;
import com.java.healSync.entities.BookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final AmbulanceRepository ambulanceRepository;
    private final BookingRepository bookingRepository;

    public void bookAmbulance(BookingRequest request) {
        Ambulance ambulance = ambulanceRepository.findById(request.getAmbulanceId())
                .orElseThrow(() -> new RuntimeException("Ambulance not found"));

        if (!ambulance.isAvailable()) {
            throw new RuntimeException("Ambulance is not available");
        }

        Booking booking = new Booking();
        booking.setPatientName(request.getPatientName());
        booking.setAmbulance(ambulance);
        booking.setPickupLocation(request.getPickupLocation());
        booking.setDropLocation(request.getDropLocation());
        booking.setBookedAt(LocalDateTime.now());

        ambulance.setAvailable(false);
        ambulance.setBookedAt(LocalDateTime.now()); // make unavailable
        bookingRepository.save(booking);
        ambulanceRepository.save(ambulance);

        // schedule availability restoration after 30 min
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ambulance.setAvailable(true);
                ambulanceRepository.save(ambulance);
            }
        }, Duration.ofMinutes(30).toMillis());
    }

    public List<Ambulance> getAvailableAmbulancesByType(String type) {
        return ambulanceRepository.findByTypeAndAvailableTrue(type.toUpperCase());
    }
}
