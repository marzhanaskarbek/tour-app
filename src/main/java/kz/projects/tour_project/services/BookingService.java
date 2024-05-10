package kz.projects.tour_project.services;

import kz.projects.tour_project.enums.BookingStatus;
import kz.projects.tour_project.models.Booking;
import kz.projects.tour_project.models.Tour;
import kz.projects.tour_project.models.User;
import kz.projects.tour_project.repositories.BookingRepository;
import kz.projects.tour_project.repositories.TourRepository;
import kz.projects.tour_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private UserService userService;


    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    public Booking changeStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setStatus(BookingStatus.valueOf(status));
            return bookingRepository.save(booking);
        }
        return null;
    }

    public void bookTour(Long tourId) {
        Tour tour = tourRepository.findById(tourId).orElse(null);
        User user = userService.getCurrentSessionUser();

        if (tour != null && user != null) {
            boolean hasActiveBooking = bookingRepository.existsByUserAndTour(user, tour);

            if (!hasActiveBooking) {
                if (tour.getAvailableSeats() > 0) {
                    Booking booking = new Booking();
                    booking.setUser(user);
                    booking.setStatus(BookingStatus.PENDING);
                    booking.setTotalPrice(tour.getPrice());
                    booking.setTour(tour);
                    bookingRepository.save(booking);

                    tour.setAvailableSeats(tour.getAvailableSeats() - 1);
                    tourRepository.save(tour);
                } else {
                    throw new RuntimeException("Недостаточно доступных мест для бронирования тура.");
                }
            } else {
                throw new RuntimeException("У вас уже есть активная бронь на этот тур.");
            }
        } else {
            throw new RuntimeException("Тур или пользователь не найдены.");
        }
    }


    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findAllByUser(user);
    }

    public void cancelBooking(Long bookingId) {
        Tour tour = bookingRepository.findById(bookingId).orElse(null).getTour();
        tour.setAvailableSeats(tour.getAvailableSeats() + 1);
        bookingRepository.deleteById(bookingId);

    }

    public Booking getBookingForUserAndTour(User user, Tour tour) {
        return bookingRepository.findByUserAndTour(user, tour);
    }


}
