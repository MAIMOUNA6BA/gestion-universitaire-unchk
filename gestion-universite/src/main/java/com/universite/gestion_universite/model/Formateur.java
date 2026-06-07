package com.universite.gestion_universite.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "formateurs")
@Data
public class Formateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String email;

    // Type : Enseignant, Enseignant associé, Responsable de formation, Tuteur
    // CORRECTION : Changé nullable = false en true pour éviter le crash lors de l'envoi depuis Angular
    @Column(nullable = true)
    private String typeFormateur;

    private String specialite;
}