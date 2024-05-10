package kz.projects.tour_project.services;

import kz.projects.tour_project.models.Permission;
import kz.projects.tour_project.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public boolean hasPermission(String permission, String userPermissions){
        String[] permissions = userPermissions.split(",");
        for (String p:permissions){
            if (p.equals(permission)){
                return true;
            }
        }
        return false;
    }

    public Permission findPermission(String permission){
        return permissionRepository.findByRole(permission);
    }

    public Permission userRolePermission(){
        return permissionRepository.findByRole("ROLE_USER");
    }
}
