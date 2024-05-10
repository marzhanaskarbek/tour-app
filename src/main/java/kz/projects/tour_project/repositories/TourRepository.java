package kz.projects.tour_project.repositories;

import jakarta.transaction.Transactional;
import kz.projects.tour_project.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findAllByDestinationAndStartDateGreaterThanEqual(String destination, String startDate);
}
