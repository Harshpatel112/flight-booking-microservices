// BookingService.java
package com.project.Service.booking.Service;

import com.project.Service.booking.Repository.BookingRepository;
import com.project.Service.booking.dto.ConfirmBookingRequest;
import com.project.Service.booking.dto.EmailRequestDTO;
import com.project.Service.booking.dto.FlightDTO;
import com.project.Service.booking.dto.SeatReservationResponse;
import com.project.Service.booking.dto.UserResponseDTO;
import com.project.Service.booking.enm.BookingStatus;
import com.project.Service.booking.exception.BookingNotFoundException;
import com.project.Service.booking.feign.FlightClient;
import com.project.Service.booking.feign.NotificationClient;
import com.project.Service.booking.feign.UserClient;
import com.project.Service.booking.model.Booking;
import com.project.Service.booking.model.Passenger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightClient flightClient;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository, 
                          FlightClient flightClient,
                          UserClient userClient,
                          NotificationClient notificationClient) {
        this.bookingRepository = bookingRepository;
        this.flightClient = flightClient;
        this.userClient = userClient;
        this.notificationClient = notificationClient;
    }

    public synchronized Booking createBooking(Booking booking) {
        logger.info("Creating booking for User ID: {}", booking.getUserId());

        FlightDTO flight = flightClient.getFlightByNumber(booking.getFlightNumber());
        if (flight == null) {
            throw new BookingNotFoundException("Flight with number " + booking.getFlightNumber() + " not found");
        }

        String seatClass = booking.getSeatClass();
        int requestedSeats = booking.getPassengersInfo().size();

        Integer availableSeats = flight.getAvailableSeatsPerClass().getOrDefault(seatClass, 0);
        if (availableSeats < requestedSeats) {
            throw new BookingNotFoundException("Not enough " + seatClass + " seats available");
        }

        double pricePerSeat = flight.getPricePerClass().getOrDefault(seatClass, 0.0);
        booking.setTotalPrice(pricePerSeat * requestedSeats);
        booking.setPassengers(requestedSeats);
        booking.setBookingStatus(BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);
        logger.info("Booking saved with ID: {}", saved.getId());
        return saved;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
    }

    public Map<String, Object> getBookingDetails(Long id) {
        Booking booking = getBookingById(id);
        FlightDTO flight = flightClient.getFlightByNumber(booking.getFlightNumber());

        Map<String, Object> map = new HashMap<>();
        map.put("booking", booking);
        map.put("flight", flight);
        return map;
    }

    public Booking updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = getBookingById(id);
        booking.setBookingStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getBookingStatus() != BookingStatus.CANCELLED) {
            throw new IllegalStateException("Only CANCELLED bookings can be deleted");
        }
        bookingRepository.deleteById(id);
    }

    public String confirmBooking(ConfirmBookingRequest dto) {
        Booking booking = getBookingById(dto.getBookingId());

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            throw new BookingNotFoundException("Already confirmed");
        }

        SeatReservationResponse response = flightClient.reserveSeats(
                booking.getFlightNumber(), booking.getPassengers(), booking.getSeatClass());

        if (!response.isSuccess()) {
            throw new BookingNotFoundException("Seat reservation failed: " + response.getMessage());
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        UserResponseDTO user = userClient.getUserById(booking.getUserId());
        EmailRequestDTO email = new EmailRequestDTO(user.getEmail(), "Booking Confirmed",
                "Your booking #" + booking.getId() + " is confirmed.");
        notificationClient.sendEmail(email);

        return "Booking Confirmed & Email Sent";
    }
    
    public List<Booking> getByFlightNumber(String flightNumber) {
        return bookingRepository.findByFlightNumber(flightNumber);
    }

    public List<Booking> getByBookingStatus(BookingStatus status) {
        return bookingRepository.findByBookingStatus(status);
    }
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }


}
