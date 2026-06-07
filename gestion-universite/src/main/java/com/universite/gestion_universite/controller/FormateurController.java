package com.universite.gestion_universite.controller;

import com.universite.gestion_universite.model.Formateur;
import com.universite.gestion_universite.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formateurs")
@CrossOrigin(origins = "*")
public class FormateurController {

    @Autowired
    private FormateurRepository formateurRepository;

    // 1. Récupérer tous les formateurs
    @GetMapping
    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }

    // 2. Enregistrer un formateur
    @PostMapping
    public ResponseEntity<Formateur> createFormateur(@RequestBody Formateur formateur) {
        try {
            Formateur sauvegarde = formateurRepository.save(formateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(sauvegarde);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}