package com.doctor_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.*;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    private String specialization;

    private Integer experienceYears;

    @Column(nullable = false)
    private Double consultationFee;

    private String hospitalName;

    private String addressLine;

    private String area;

    private String city;

    private String state;

    private String country;

    private String pincode;

    private LocalDateTime createdAt;

    private String image;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DoctorAvailability> availabilities = new ArrayList<>();
}