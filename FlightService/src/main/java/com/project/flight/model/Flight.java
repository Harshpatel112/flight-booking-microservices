package com.project.flight.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @Column(unique = true, nullable = false)
    private String flightNumber;

    private String airline;

   
    @ElementCollection
    @CollectionTable(name = "flight_price_per_class", joinColumns = @JoinColumn(name = "flight_id"))
    @MapKeyColumn(name = "seat_class") // Example: ECONOMY, BUSINESS
    @Column(name = "price")
    private Map<String, Double> pricePerClass;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightSchedule> schedules;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    public Flight() {}

    public Flight(Long flightId, String flightNumber, String airline) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.airline = airline;
    }

    // ✅ Getters & Setters
    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Map<String, Double> getPricePerClass() {
        return pricePerClass;
    }

    public void setPricePerClass(Map<String, Double> pricePerClass) {
        this.pricePerClass = pricePerClass;
    }

    public List<FlightSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<FlightSchedule> schedules) {
        this.schedules = schedules;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
