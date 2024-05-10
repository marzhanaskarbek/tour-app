package kz.projects.tour_project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "t_tours")
@Getter
@Setter
public class Tour extends BaseModel{

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "price")
    private Double price;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "startDate")
    private String startDate;

    @Column(name = "endDate")
    private String endDate;

    @Column(name = "destination")
    private String destination;

    @Column(name = "availableSeats")
    private Integer availableSeats;


    public String loadImage(){
        if (imageUrl == null || imageUrl.isEmpty()) {
            return "/defaults/default.png";
        }
        return imageUrl;
    }

    public void setStartDateFormatted(String startDateFormatted) {
        this.startDate = startDateFormatted;
    }

    public void setEndDateFormatted(String endDateFormatted) {
        this.startDate = endDateFormatted;
    }

    @PostPersist
    public void formatDate() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        startDate = LocalDate.parse(startDate, inputFormatter).format(outputFormatter);
        endDate = LocalDate.parse(endDate, inputFormatter).format(outputFormatter);
    }
}
