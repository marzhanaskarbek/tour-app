package kz.projects.tour_project.services;

import kz.projects.tour_project.models.Tour;
import kz.projects.tour_project.repositories.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;


    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour saveTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }

    public void deleteTourById(Long id) {
        tourRepository.deleteById(id);
    }

    public List<Tour> searchTours(String destination, String startDate) {
        return tourRepository.findAllByDestinationAndStartDateGreaterThanEqual(destination, startDate);
    }
}
