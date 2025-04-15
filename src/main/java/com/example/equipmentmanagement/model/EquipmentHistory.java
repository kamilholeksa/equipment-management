package com.example.equipmentmanagement.model;

import com.example.equipmentmanagement.enumeration.EquipmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Equipment equipment;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus newStatus;

    private String oldLocation;

    private String newLocation;

    @ManyToOne
    private User oldUser;

    @ManyToOne
    private User newUser;

    @Column(nullable = false)
    private Instant timestamp;

}
