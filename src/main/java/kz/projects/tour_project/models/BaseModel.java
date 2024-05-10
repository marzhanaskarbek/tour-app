package kz.projects.tour_project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    public void setCreatedAt(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt(){
        updatedAt = LocalDateTime.now();
    }
}
