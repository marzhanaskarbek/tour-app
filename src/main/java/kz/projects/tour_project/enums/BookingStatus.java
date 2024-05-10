package kz.projects.tour_project.enums;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED;

    public static BookingStatus[] getAllStatuses() {
        return values();
    }
}

