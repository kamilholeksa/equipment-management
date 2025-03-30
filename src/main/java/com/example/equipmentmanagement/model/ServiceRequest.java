package com.example.equipmentmanagement.model;

import com.example.equipmentmanagement.model.enumeration.ServiceRequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "service-request-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "equipment", subgraph = "equipment"),
                @NamedAttributeNode(value = "user", subgraph = "user"),
                @NamedAttributeNode(value = "technician", subgraph = "user"),
        },
        subgraphs = {
                @NamedSubgraph(name = "equipment", attributeNodes = {
                        @NamedAttributeNode(value = "type"),
                        @NamedAttributeNode(value = "address"),
                        @NamedAttributeNode(value = "user", subgraph = "user"),
                }),
                @NamedSubgraph(name = "user", attributeNodes = @NamedAttributeNode("roles")),
        }
)
public class ServiceRequest extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceRequestStatus status;

    private String closeInfo;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties({"type", "address", "user"})
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private User user;

    @ManyToOne
    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private User technician;

    @OneToMany(mappedBy = "serviceRequest")
    private Set<ServiceRequestNote> noteSet = new HashSet<>();

}
