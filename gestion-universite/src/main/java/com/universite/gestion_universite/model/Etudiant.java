package com.universite.gestion_universite.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "etudiants")
@Data // Génère automatiquement les Getters/Setters grâce à Lombok
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ine; // Identifiant National Étudiant ou ID Étudiant

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    private LocalDate dateNaissance;

    private String formation;

    private String promo;

    private Integer anneeDebut;

    private Integer anneeSortie;

    private String diplomes;

    private String autresFormations;
}