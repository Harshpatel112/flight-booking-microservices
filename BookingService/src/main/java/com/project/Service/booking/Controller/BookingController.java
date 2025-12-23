package com.project.Service.booking.Controller;

import com.project.Service.booking.Service.BookingService;
import com.project.Service.booking.dto.BookingRequestDTO;
import com.project.Service.booking.dto.BookingResponseDTO;
import com.project.Service.booking.enm.BookingStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Booking Management", description = "APIs for flight booking management")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create new booking", description = "Create a new flight booking with passenger details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Booking created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid booking data"),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "409", description = "Seats not available")
    })
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequest) {
        BookingResponseDTO response = bookingService.createBookingAdvanced(bookingRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking details", description = "Get comprehensive booking details by booking ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<BookingResponseDTO> getBookingDetails(@PathVariable String bookingId) {
        BookingResponseDTO response = bookingService.getBookingDetailsAdvanced(bookingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pnr/{pnr}")
    @Operation(summary = "Get booking by PNR", description = "Get booking details by PNR number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking found successfully"),
        @ApiResponse(responseCode = "404", description = "PNR not found")
    })
    public ResponseEntity<BookingResponseDTO> getBookingByPNR(@PathVariable String pnr) {
        BookingResponseDTO response = bookingService.getBookingByPNR(pnr);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user bookings", description = "Get all bookings for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User bookings retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookingDateTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder) {
        
        List<BookingResponseDTO> bookings = bookingService.getUserBookingsAdvanced(userId, page, size, sortBy, sortOrder);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{bookingId}/confirm")
    @Operation(summary = "Confirm booking", description = "Confirm booking after payment completion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking confirmed successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found"),
        @ApiResponse(responseCode = "400", description = "Booking cannot be confirmed")
    })
    public ResponseEntity<BookingResponseDTO> confirmBooking(
            @PathVariable String bookingId,
            @RequestParam String transactionId) {
        
        BookingResponseDTO response = bookingService.confirmBookingAdvanced(bookingId, transactionId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel booking", description = "Cancel existing booking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found"),
        @ApiResponse(responseCode = "400", description = "Booking cannot be cancelled")
    })
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable String bookingId,
            @RequestParam String cancellationReason) {
        
        BookingResponseDTO response = bookingService.cancelBookingAdvanced(bookingId, cancellationReason);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{bookingId}/status")
    @Operation(summary = "Update booking status", description = "Update booking status (Admin only)")
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(
            @PathVariable String bookingId,
            @RequestParam BookingStatus status) {
        
        BookingResponseDTO response = bookingService.updateBookingStatusAdvanced(bookingId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/flight/{flightNumber}")
    @Operation(summary = "Get bookings by flight", description = "Get all bookings for a specific flight")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByFlight(@PathVariable String flightNumber) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByFlightNumber(flightNumber);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get bookings by status", description = "Get all bookings with specific status")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByStatus(@PathVariable BookingStatus status) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}/check-in")
    @Operation(summary = "Check-in eligibility", description = "Check if booking is eligible for check-in")
    public ResponseEntity<Boolean> canCheckIn(@PathVariable String bookingId) {
        boolean canCheckIn = bookingService.canCheckIn(bookingId);
        return ResponseEntity.ok(canCheckIn);
    }

    @PostMapping("/{bookingId}/check-in")
    @Operation(summary = "Web check-in", description = "Perform web check-in for booking")
    public ResponseEntity<BookingResponseDTO> performCheckIn(@PathVariable String bookingId) {
        BookingResponseDTO response = bookingService.performWebCheckIn(bookingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookingId}/boarding-pass")
    @Operation(summary = "Get boarding pass", description = "Generate and get boarding pass")
    public ResponseEntity<String> getBoardingPass(@PathVariable String bookingId) {
        String boardingPassUrl = bookingService.generateBoardingPass(bookingId);
        return ResponseEntity.ok(boardingPassUrl);
    }

    @GetMapping("/{bookingId}/e-ticket")
    @Operation(summary = "Get e-ticket", description = "Generate and get e-ticket")
    public ResponseEntity<String> getETicket(@PathVariable String bookingId) {
        String eTicketUrl = bookingService.generateETicket(bookingId);
        return ResponseEntity.ok(eTicketUrl);
    }

    // Admin APIs
    @GetMapping("/all")
    @Operation(summary = "Get all bookings", description = "Get all bookings with pagination (Admin only)")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "bookingDateTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder) {
        
        List<BookingResponseDTO> bookings = bookingService.getAllBookingsAdvanced(page, size, sortBy, sortOrder);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/analytics/count")
    @Operation(summary = "Get booking count", description = "Get total booking count")
    public ResponseEntity<Long> getBookingCount() {
        return ResponseEntity.ok(bookingService.getTotalBookingCount());
    }
}
