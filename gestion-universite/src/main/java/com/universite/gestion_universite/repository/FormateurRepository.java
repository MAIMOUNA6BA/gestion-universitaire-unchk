package com.universite.gestion_universite.repository;

import com.universite.gestion_universite.model.Formateur; // 👈 On cible le dossier model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
}