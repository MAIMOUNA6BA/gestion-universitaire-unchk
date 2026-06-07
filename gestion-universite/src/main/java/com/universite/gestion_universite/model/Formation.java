package com.universite.gestion_universite.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "formations")
@Data
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codeFormation; // Ex: MIL (Master Ingénierie Logicielle)

    @Column(nullable = false)
    private String nomFormation;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    private String typeFormation; // Ex: Initiale, Continue, Certification
    private String niveau; // Ex: Licence, Master

    private Double montantFinancement;
    private String typeFinancement; // Ex: Privé, Bourse d'État
}