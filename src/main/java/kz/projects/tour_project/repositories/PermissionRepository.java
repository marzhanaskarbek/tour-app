package kz.projects.tour_project.repositories;

import kz.projects.tour_project.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByRole(String role);
}
