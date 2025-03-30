package com.example.equipmentmanagement.model;

import com.example.equipmentmanagement.model.enumeration.EquipmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "equipment-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "type"),
                @NamedAttributeNode(value = "address"),
                @NamedAttributeNode(value = "user", subgraph = "user.roles")
        },
        subgraphs = @NamedSubgraph(name = "user.roles", attributeNodes = @NamedAttributeNode("roles"))
)
@Entity
public class Equipment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    private String description;

    @Column(nullable = false)
    private String inventoryNumber;

    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatus status;

    private String location;

    private LocalDate purchaseDate;

    private LocalDate warrantyUntil;

    private LocalDate withdrawalDate;

    @ManyToOne
    @JoinColumn(name = "equipment_type_id")
    @JsonIgnoreProperties({"description", "equipmentSet"})
    private EquipmentType type;

    @ManyToOne
    private Address address;

    @ManyToOne
    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private User user;

}
