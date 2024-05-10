package kz.projects.tour_project.controllers;

import kz.projects.tour_project.enums.BookingStatus;
import kz.projects.tour_project.enums.Destinations;
import kz.projects.tour_project.models.Booking;
import kz.projects.tour_project.models.Tour;
import kz.projects.tour_project.services.BookingService;
import kz.projects.tour_project.services.FileStorageService;
import kz.projects.tour_project.services.TourService;
import kz.projects.tour_project.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
public class AdminController {

    @Value("${tours.images.path}")
    private String toursImagesPath;

    private final TourService tourService;

    private final UserService userService;

    private final BookingService bookingService;

    private final FileStorageService fileStorageService;


    @GetMapping
    public String index(){
        return "admin/index";
    }

    @GetMapping("/users")
    public String users(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users/index";
    }

    @GetMapping("/tours")
    public String tours(Model model){
        model.addAttribute("tours", tourService.getAllTours());
        return "admin/tours/index";
    }

    @GetMapping("/bookings")
    public String bookings(Model model){
        BookingStatus[] statuses = BookingStatus.getAllStatuses();
        model.addAttribute("bookingStatuses", statuses);
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings/index";
    }


    // tours functions

    @GetMapping("/tours/create")
    public String createTour(Model model) {
        model.addAttribute("destinations", Destinations.values());
        return "admin/tours/create";
    }

    @PostMapping("/tours/delete/{id}")
    public String deleteTour(@PathVariable Long id) {
        tourService.deleteTourById(id);
        return "redirect:/tours";
    }

    @GetMapping("/tours/edit/{id}")
    public String editTour(@PathVariable Long id, Model model) {
        if (tourService.getTourById(id) != null) {
            Tour tour = tourService.getTourById(id);
            model.addAttribute("tour", tour);
            model.addAttribute("destinations", Destinations.values());
            return "admin/tours/edit";
        } else {
            return "redirect:/error?error";
        }
    }

    @PostMapping("/tours/update")
    public String updateTour(@RequestParam(name = "tour_id") Long id,
                             @RequestParam(name = "title") String title,
                             @RequestParam(name = "description") String description,
                             @RequestParam(name = "short_description") String shortDescription,
                             @RequestParam(name = "price") Double price,
                             @RequestParam(name = "imageUrl") MultipartFile imageUrl,
                             @RequestParam(name = "startDate") String startDate,
                             @RequestParam(name = "endDate") String endDate,
                             @RequestParam(name = "destination") String destination,
                             @RequestParam(name = "availableSeats") Integer availableSeats){
        try {
            Tour tour = tourService.getTourById(id);

            tour.setTitle(title);
            tour.setDescription(description);
            tour.setShortDescription(shortDescription);
            tour.setPrice(price);
            tour.setStartDate(startDate);
            tour.setEndDate(endDate);
            tour.setDestination(destination);
            tour.setAvailableSeats(availableSeats);

            if (!imageUrl.isEmpty()) {
                String fileName = fileStorageService.saveFile(imageUrl, toursImagesPath);
                tour.setImageUrl(fileName);
            }

            Tour newTour = tourService.saveTour(tour);

            if (newTour != null) {
                return "redirect:/tours?success";
            } else {
                return "redirect:/tours?error";
            }

        } catch (Exception e) {
            return "redirect:/error?error";
        }
    }

    // booking functions

    @PostMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBookingById(id);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/bookings/edit/{id}")
    public String editBooking(@PathVariable Long id, Model model) {
        if (bookingService.getBookingById(id) != null) {
            Booking booking = bookingService.getBookingById(id);
            model.addAttribute("bookingStatuses", BookingStatus.getAllStatuses());
            model.addAttribute("booking", booking);
            return "admin/bookings/edit";
        } else {
            return "redirect:/error?error";
        }
    }

    @PutMapping("/bookings/update")
    public String updateBooking(@RequestParam(name = "booking_id") Long id,
                                @RequestParam(name = "status") BookingStatus status){
        try {
            Booking booking = bookingService.getBookingById(id);
            booking.setStatus(status);
            Booking newBooking = bookingService.saveBooking(booking);

            if (newBooking != null) {
                return "redirect:/bookings?success";
            } else {
                return "redirect:/bookings?error";
            }

        } catch (Exception e) {
            return "redirect:/error?error";
        }
    }


    // user functions

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/ban/{id}")
    public String banUser(@PathVariable Long id) {
        userService.banUserById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/unban/{id}")
    public String unbanUser(@PathVariable Long id) {
        userService.unbanUserById(id);
        return "redirect:/admin/users";
    }
}
