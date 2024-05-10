package kz.projects.tour_project.components;

import kz.projects.tour_project.models.Permission;
import kz.projects.tour_project.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Autowired
    public DataInitializer(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Permission> existingPermissions = permissionRepository.findAll();

        if (existingPermissions.isEmpty()) {
            Permission role1 = new Permission();
            role1.setRole("ROLE_USER");

            Permission role2 = new Permission();
            role2.setRole("ROLE_ADMIN");

            permissionRepository.save(role1);
            permissionRepository.save(role2);

            System.out.println("Initial permissions saved successfully.");
        } else {
            System.out.println("Permissions already exist. Skipping initialization.");
        }
    }
}