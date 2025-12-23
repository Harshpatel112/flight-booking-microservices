package com.project.Service.booking.feign;

import com.project.Service.booking.dto.FlightDTO;
import com.project.Service.booking.dto.SeatReservationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class FlightClientFallback implements FlightClient {

    @Override
    public CompletableFuture<FlightDTO> getFlightByNumber(String flightNumber) {
        log.warn("Flight service is unavailable. Using fallback for flight: {}", flightNumber);
        
        FlightDTO fallbackFlight = new FlightDTO();
        fallbackFlight.setFlightNumber(flightNumber);
        fallbackFlight.setStatus("SERVICE_UNAVAILABLE");
        fallbackFlight.setAirline("Unknown");
        fallbackFlight.setOrigin("Unknown");
        fallbackFlight.setDestination("Unknown");
        
        return CompletableFuture.completedFuture(fallbackFlight);
    }

    @Override
    public CompletableFuture<SeatReservationResponse> reserveSeats(String flightNumber, int passengers, String seatClass) {
        log.warn("Flight service is unavailable. Cannot reserve seats for flight: {}", flightNumber);
        
        SeatReservationResponse fallbackResponse = new SeatReservationResponse();
        fallbackResponse.setSuccess(false);
        fallbackResponse.setMessage("Flight service is temporarily unavailable. Please try again in a few minutes.");
        fallbackResponse.setFlightNumber(flightNumber);
        
        return CompletableFuture.completedFuture(fallbackResponse);
    }
}