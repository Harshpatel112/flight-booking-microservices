package com.project.flight.Flight_Controller;

import com.project.flight.Flight_Service.FlightService;
import com.project.flight.dto.FlightDetailsDTO;
import com.project.flight.dto.FlightSearchRequestDTO;
import com.project.flight.dto.FlightSearchResponseDTO;
import com.project.flight.dto.SeatMapDTO;
import com.project.flight.enm.FlightStatus;
import com.project.flight.model.Flight;
import com.project.flight.model.FlightSchedule;
import com.project.flight.model.Seat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@Tag(name = "Flight Management", description = "APIs for flight search, booking and management")
@CrossOrigin(origins = "*")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/search")
    @Operation(summary = "Search flights", description = "Search flights with comprehensive filters and options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flights found successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "404", description = "No flights found")
    })
    public ResponseEntity<FlightSearchResponseDTO> searchFlights(@Valid @RequestBody FlightSearchRequestDTO searchRequest) {
        FlightSearchResponseDTO response = flightService.searchFlightsAdvanced(searchRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Simple flight search", description = "Simple flight search with basic parameters")
    public ResponseEntity<List<FlightDetailsDTO>> searchFlightsSimple(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(defaultValue = "0") int children,
            @RequestParam(defaultValue = "0") int infants,
            @RequestParam(defaultValue = "ECONOMY") String travelClass) {
        
        FlightSearchRequestDTO searchRequest = FlightSearchRequestDTO.builder()
                .origin(origin)
                .destination(destination)
                .departureDate(departureDate)
                .adults(adults)
                .children(children)
                .infants(infants)
                .travelClass(travelClass)
                .tripType("ONE_WAY")
                .build();
                
        FlightSearchResponseDTO response = flightService.searchFlightsAdvanced(searchRequest);
        return ResponseEntity.ok(response.getFlights());
    }

    @GetMapping("/flight/{flightNumber}")
    @Operation(summary = "Get flight details", description = "Get comprehensive flight details by flight number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<FlightDetailsDTO> getFlightDetails(@PathVariable String flightNumber) {
        FlightDetailsDTO response = flightService.getFlightDetailsByFlightNumber(flightNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/flight/{flightNumber}/seatmap")
    @Operation(summary = "Get seat map", description = "Get aircraft seat map for flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seat map retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<SeatMapDTO> getSeatMap(@PathVariable String flightNumber) {
        SeatMapDTO seatMap = flightService.getFlightSeatMap(flightNumber);
        return ResponseEntity.ok(seatMap);
    }

    @PutMapping("/reserveSeats")
    @Operation(summary = "Reserve seats", description = "Reserve seats for passengers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seats reserved successfully"),
        @ApiResponse(responseCode = "400", description = "Seats not available"),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<com.project.Service.booking.dto.SeatReservationResponse> reserveSeats(
            @RequestParam String flightNumber,
            @RequestParam String seatClass,
            @RequestParam int passengers) {
        
        com.project.Service.booking.dto.SeatReservationResponse response = 
            flightService.reserveSeatsAdvanced(flightNumber, seatClass, passengers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/details")
    @Operation(summary = "Get all flight details", description = "Get all available flights with details")
    public ResponseEntity<List<FlightDetailsDTO>> getAllFlightDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "departureTime") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {
        
        List<FlightDetailsDTO> flights = flightService.getAllFlightDetailsWithPagination(page, size, sortBy, sortOrder);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/airlines")
    @Operation(summary = "Get all airlines", description = "Get list of all available airlines")
    public ResponseEntity<List<String>> getAllAirlines() {
        List<String> airlines = flightService.getAllAirlines();
        return ResponseEntity.ok(airlines);
    }

    @GetMapping("/airports")
    @Operation(summary = "Get all airports", description = "Get list of all available airports")
    public ResponseEntity<List<String>> getAllAirports() {
        List<String> airports = flightService.getAllAirports();
        return ResponseEntity.ok(airports);
    }

    // Admin APIs
    @PostMapping("/add")
    @Operation(summary = "Add new flight", description = "Add new flight (Admin only)")
    public ResponseEntity<Flight> addFlight(@Valid @RequestBody Flight flight) {
        Flight savedFlight = flightService.addFlight(flight);
        return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
    }

    @PostMapping("/schedule/add")
    @Operation(summary = "Add flight schedule", description = "Add new flight schedule (Admin only)")
    public ResponseEntity<FlightSchedule> addFlightSchedule(@Valid @RequestBody FlightSchedule schedule) {
        FlightSchedule savedSchedule = flightService.addFlightSchedule(schedule);
        return new ResponseEntity<>(savedSchedule, HttpStatus.CREATED);
    }

    @PatchMapping("/schedule/{id}/status")
    @Operation(summary = "Update flight status", description = "Update flight status (Admin only)")
    public ResponseEntity<String> updateFlightStatus(
            @PathVariable Long id,
            @RequestParam FlightStatus status) {
        flightService.updateFlightStatus(id, status);
        return ResponseEntity.ok("Flight status updated successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update flight", description = "Update flight information (Admin only)")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @Valid @RequestBody Flight updatedFlight) {
        Flight updated = flightService.updateFlight(id, updatedFlight);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{flightNumber}")
    @Operation(summary = "Delete flight", description = "Delete flight by flight number (Admin only)")
    public ResponseEntity<String> deleteByFlightNumber(@PathVariable String flightNumber) {
        flightService.deleteByFlightNumber(flightNumber);
        return ResponseEntity.ok("Flight deleted successfully");
    }

    @GetMapping("/count")
    @Operation(summary = "Get flight count", description = "Get total number of flights")
    public ResponseEntity<Long> getFlightCount() {
        return ResponseEntity.ok(flightService.getFlightCount());
    }
}
