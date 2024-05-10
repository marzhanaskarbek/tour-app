package kz.projects.tour_project.repositories;

import jakarta.transaction.Transactional;
import kz.projects.tour_project.models.Booking;
import kz.projects.tour_project.models.Tour;
import kz.projects.tour_project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findById(long id);

    boolean existsByUserAndTour(User user, Tour tour);

    List<Booking> findAllByUser(User user);

    Booking findByUserAndTour(User user, Tour tour);
}
