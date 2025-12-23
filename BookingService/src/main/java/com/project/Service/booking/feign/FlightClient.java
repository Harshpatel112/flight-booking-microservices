package com.project.Service.booking.feign;

import com.project.Service.booking.dto.FlightDTO;
import com.project.Service.booking.dto.SeatReservationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.CompletableFuture;

@FeignClient(name = "FLIGHT-SERVICE", fallback = FlightClientFallback.class)
public interface FlightClient {

    @GetMapping("/api/v1/flights/flight/{flightNumber}")
    @CircuitBreaker(name = "flightService", fallbackMethod = "getFlightByNumberFallback")
    @Retry(name = "flightService")
    @TimeLimiter(name = "flightService")
    CompletableFuture<FlightDTO> getFlightByNumber(@PathVariable("flightNumber") String flightNumber);

    @PutMapping("/api/v1/flights/reserveSeats")
    @CircuitBreaker(name = "flightService", fallbackMethod = "reserveSeatsFallback")
    @Retry(name = "flightService")
    @TimeLimiter(name = "flightService")
    CompletableFuture<SeatReservationResponse> reserveSeats(@RequestParam("flightNumber") String flightNumber,
                                                           @RequestParam("passengers") int passengers,
                                                           @RequestParam("seatClass") String seatClass);
    
    // Fallback methods
    default CompletableFuture<FlightDTO> getFlightByNumberFallback(String flightNumber, Exception ex) {
        FlightDTO fallbackFlight = new FlightDTO();
        fallbackFlight.setFlightNumber(flightNumber);
        fallbackFlight.setStatus("UNAVAILABLE");
        return CompletableFuture.completedFuture(fallbackFlight);
    }
    
    default CompletableFuture<SeatReservationResponse> reserveSeatsFallback(String flightNumber, int passengers, String seatClass, Exception ex) {
        SeatReservationResponse fallbackResponse = new SeatReservationResponse();
        fallbackResponse.setSuccess(false);
        fallbackResponse.setMessage("Flight service temporarily unavailable. Please try again later.");
        return CompletableFuture.completedFuture(fallbackResponse);
    }
}
