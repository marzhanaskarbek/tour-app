package kz.projects.tour_project.controllers;

import kz.projects.tour_project.models.Tour;
import kz.projects.tour_project.services.BookingService;
import kz.projects.tour_project.services.FileStorageService;
import kz.projects.tour_project.services.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/bookings")
@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public String tours(Model model) {
        model.addAttribute("tours", bookingService.getAllBookings());
        return "tours/index";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/book-tour/{tourId}")
    public String bookTour(@PathVariable Long tourId) {
        bookingService.bookTour(tourId);
        return "redirect:/profile/tours";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable(name = "bookingId") Long bookingId){
        bookingService.cancelBooking(bookingId);
        return "redirect:/profile/tours";
    }
}

