package kz.projects.tour_project.repositories;

import jakarta.transaction.Transactional;
import kz.projects.tour_project.models.Booking;
import kz.projects.tour_project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
