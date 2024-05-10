package kz.projects.tour_project.models;

import jakarta.persistence.*;
import kz.projects.tour_project.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_bookings")
@Getter
@Setter
public class Booking extends BaseModel{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    private LocalDateTime bookingDate;
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // Enum: PENDING, CONFIRMED, CANCELLED, COMPLETED

    public Booking() {
        this.bookingDate = LocalDateTime.now();
    }
}
