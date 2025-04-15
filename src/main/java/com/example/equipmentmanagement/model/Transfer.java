package com.example.equipmentmanagement.model;

import com.example.equipmentmanagement.enumeration.TransferStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "transfer-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "equipment", subgraph = "equipment"),
                @NamedAttributeNode(value = "transferor", subgraph = "user"),
                @NamedAttributeNode(value = "obtainer", subgraph = "user"),
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
public class Transfer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate requestDate;

    private LocalDate decisionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties({"type", "address", "user"})
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private User transferor;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIncludeProperties({"firstName", "lastName", "username"})
    private User obtainer;

}
