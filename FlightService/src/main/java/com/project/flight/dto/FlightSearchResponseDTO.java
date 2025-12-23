package com.project.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchResponseDTO {
    
    // Search Information
    private String searchId;
    private FlightSearchCriteriaDTO searchCriteria;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime searchTimestamp;
    
    // Results Summary
    private int totalFlights;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    
    // Flight Results
    private List<FlightDetailsDTO> flights;
    
    // Filters and Sorting
    private FlightFiltersDTO availableFilters;
    private String appliedSortBy;
    private String appliedSortOrder;
    
    // Price Information
    private PriceRangeDTO priceRange;
    
    // Additional Information
    private List<String> airlines;
    private List<String> airports;
    private Map<String, Integer> flightsByAirline;
    private Map<String, Integer> flightsByDuration;
    
    // Recommendations
    private List<FlightDetailsDTO> recommendedFlights;
    private List<FlightDetailsDTO> cheapestFlights;
    private List<FlightDetailsDTO> fastestFlights;
    
    // Search Metadata
    private boolean hasMoreResults;
    private String nextPageToken;
    private long searchDurationMs;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightSearchCriteriaDTO {
        private String origin;
        private String destination;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate departureDate;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate returnDate;
        
        private int adults;
        private int children;
        private int infants;
        private String travelClass;
        private String tripType;
        private boolean directFlightsOnly;
        private String preferredAirline;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightFiltersDTO {
        private List<AirlineFilterDTO> airlines;
        private List<String> departureTimeSlots;
        private List<String> arrivalTimeSlots;
        private List<Integer> stopOptions; // 0, 1, 2+ stops
        private PriceRangeDTO priceRange;
        private DurationRangeDTO durationRange;
        private List<String> aircraftTypes;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AirlineFilterDTO {
            private String airlineCode;
            private String airlineName;
            private int flightCount;
            private BigDecimal minPrice;
        }
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DurationRangeDTO {
            private int minDurationMinutes;
            private int maxDurationMinutes;
            private String minDurationFormatted;
            private String maxDurationFormatted;
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRangeDTO {
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private String currency;
        private BigDecimal averagePrice;
    }
}