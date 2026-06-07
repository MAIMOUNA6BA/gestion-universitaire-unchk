package com.universite.gestion_universite.service;

import com.universite.gestion_universite.model.Etudiant;
import com.universite.gestion_universite.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    // Récupérer tous les étudiants (pour les tableaux du Dashboard)
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    // Récupérer un étudiant par son ID
    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }

    // Récupérer un étudiant par son INE (recherche spécifique)
    public Optional<Etudiant> getEtudiantByIne(String ine) {
        return etudiantRepository.findByIne(ine);
    }

    // Enregistrer ou modifier un étudiant
    public Etudiant saveEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    // Supprimer un étudiant
    public void deleteEtudiant(Long id) {
        etudiantRepository.deleteById(id);
    }
}