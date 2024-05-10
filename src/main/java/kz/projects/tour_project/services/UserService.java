package kz.projects.tour_project.services;

import kz.projects.tour_project.models.Booking;
import kz.projects.tour_project.models.Permission;
import kz.projects.tour_project.models.User;
import kz.projects.tour_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if (user != null){
            if (user.isBanned()){
                throw new UsernameNotFoundException("User is banned");
            }
            return user;
        } else {
            throw new UsernameNotFoundException("User not found");
        }

    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User addUser(User user){
        User userFromDB = userRepository.findByEmail(user.getEmail());

        if (userFromDB != null){
            return null;
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setPermissions(new HashSet<>());
            Permission userRolePermission = permissionService.userRolePermission();
            user.getPermissions().add(userRolePermission);
            return userRepository.save(user);
        }

    }

    public User getCurrentSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    public void banUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user != null){
            user.setBanned(true);
            userRepository.save(user);
        }
    }

    public void unbanUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user != null){
            user.setBanned(false);
            userRepository.save(user);
        }
    }
}
