package kz.projects.tour_project;

import kz.projects.tour_project.models.Permission;
import kz.projects.tour_project.repositories.PermissionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TourProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourProjectApplication.class, args);
	}
}
