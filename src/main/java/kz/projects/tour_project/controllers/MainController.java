package kz.projects.tour_project.controllers;

import kz.projects.tour_project.enums.Destinations;
import kz.projects.tour_project.models.User;
import kz.projects.tour_project.services.BookingService;
import kz.projects.tour_project.services.PermissionService;
import kz.projects.tour_project.services.TourService;
import kz.projects.tour_project.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final PermissionService permissionService;
    private final TourService tourService;
    private final BookingService bookingService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("destinations", Destinations.values());
        model.addAttribute("tours", tourService.getAllTours());
        return "index";
    }

    @GetMapping("/pricing")
    public String about(Model model){
        model.addAttribute("destinations", Destinations.values());
        model.addAttribute("tours", tourService.getAllTours());
        return "pricing";
    }

    @GetMapping("/contacts")
    public String contact(Model model){
        model.addAttribute("destinations", Destinations.values());
        model.addAttribute("tours", tourService.getAllTours());
        return "contacts";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model){
        model.addAttribute("destinations", Destinations.values());
        model.addAttribute("tours", tourService.getAllTours());
        User user = userService.getCurrentSessionUser();
        model.addAttribute("user", user);
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/tours")
    public String profileTours(Model model){
        User user = userService.getCurrentSessionUser();
        model.addAttribute("destinations", Destinations.values());
        model.addAttribute("tours", tourService.getAllTours());
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.getBookingsByUser(user));
        return "profile-tours";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/basket")
    public String userBasket(Model model){
        User user = userService.getCurrentSessionUser();
        model.addAttribute("destinations", Destinations.values());
        model.addAttribute("tours", tourService.getAllTours());
        model.addAttribute("bookings", bookingService.getBookingsByUser(user));
        return "basket";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/sign-in")
    public String signIn(){
        return "auth/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/sign-up")
    public String signUp(){
        return "auth/register";
    }

    @PostMapping("/to-sign-up")
    public String toSignUp(@RequestParam(name = "user_firstname") String firstName,
                           @RequestParam(name = "user_lastname") String lastName,
                           @RequestParam(name = "user_username") String username,
                           @RequestParam(name = "user_email") String email,
                           @RequestParam(name = "user_phone") String phone,
                           @RequestParam(name = "user_password") String password,
                           @RequestParam(name = "user_repeat_password") String repeat_password){
        if (password.equals(repeat_password)){
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setEmail(email);
            user.setPhone(phone);
            user.setBanned(false);
            user.setPassword(password);
            User newUser = userService.addUser(user);

            if (newUser != null){
                newUser.getPermissions().add(permissionService.userRolePermission());
                return "redirect:/sign-in?success";
            }
            else{
                return "redirect:/sign-up?error";
            }
        }
        else{
            return "redirect:/sign-up?passwords_mismatch";
        }
    }
}
