package com.chronoai.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data // Lombok annotation to create getters, setters, toString, etc.
@Entity // Marks this class as a JPA entity (a table in the DB)
@Table(name = "tasks") // Specifies the table name
public class Task {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cronExpression;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING) // Stores the enum as a string (e.g., "EMAIL")
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private String notificationTarget; // This will be an email address or a webhook URL

    private boolean enabled = true;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Enum to define the types of notifications we support
    public enum NotificationType {
        EMAIL,
        WEBHOOK
    }
}