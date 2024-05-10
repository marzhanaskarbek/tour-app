package kz.projects.tour_project.controllers;

import kz.projects.tour_project.enums.Destinations;
import kz.projects.tour_project.models.Booking;
import kz.projects.tour_project.models.Tour;
import kz.projects.tour_project.models.User;
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

import java.util.List;

@RequestMapping("/tours")
@Controller
@RequiredArgsConstructor
public class TourController {

    @Value("${tours.images.path}")
    private String toursImagesPath;

    private final TourService tourService;

    private final UserService userService;

    private final BookingService bookingService;

    private final FileStorageService fileStorageService;

    @GetMapping
    public String tours(Model model) {
        model.addAttribute("destinations", Destinations.values());
        model.addAttribute("tours", tourService.getAllTours());
        return "tours/index";
    }

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @PostMapping("/store")
    public String addTour(@RequestParam(name = "title") String title,
                          @RequestParam(name = "description") String description,
                          @RequestParam(name = "short_description") String shortDescription,
                          @RequestParam(name = "price") Double price,
                          @RequestParam(name = "imageUrl") MultipartFile imageUrl,
                          @RequestParam(name = "startDate") String startDate,
                          @RequestParam(name = "endDate") String endDate,
                          @RequestParam(name = "destination") String destination,
                          @RequestParam(name = "availableSeats") Integer availableSeats){
        try {
            Tour tour = new Tour();
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

    @GetMapping("/{id}")
    public String tour(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        if (tour != null) {
            model.addAttribute("tour", tour);

            User user = userService.getCurrentSessionUser();

            Booking booking = bookingService.getBookingForUserAndTour(user, tour);
            model.addAttribute("booking", booking);

            boolean userHasBooking = booking != null && booking.getUser().getId().equals(user.getId());
            model.addAttribute("userHasBooking", userHasBooking);
            model.addAttribute("destinations", Destinations.values());
            model.addAttribute("tours", tourService.getAllTours());

            return "tours/show";
        } else {
            return "redirect:/error?error";
        }
    }

    @GetMapping("/search")
    public String searchTours(@RequestParam(name = "destination", required = false) String destination,
                              @RequestParam(name = "date-start", required = false) String startDate,
                              Model model) {
        List<Tour> tours = tourService.searchTours(destination, startDate);
        model.addAttribute("destinations", Destinations.values());
        model.addAttribute("tours", tours);
        return "tours/search-results";
    }

}

