package com.universite.gestion_universite.controller;

import com.universite.gestion_universite.model.Etudiant;
import com.universite.gestion_universite.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "*") // Permet d'accepter les requêtes venant d'autres serveurs (Express/Angular)
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    // GET : http://localhost:8080/api/etudiants
    @GetMapping
    public List<Etudiant> getAllEtudiants() {
        return etudiantService.getAllEtudiants();
    }

    // GET : http://localhost:8080/api/etudiants/1
    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        return etudiantService.getEtudiantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET : http://localhost:8080/api/etudiants/ine/MON_INE_123
    @GetMapping("/ine/{ine}")
    public ResponseEntity<Etudiant> getEtudiantByIne(@PathVariable String ine) {
        return etudiantService.getEtudiantByIne(ine)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST : http://localhost:8080/api/etudiants (Pour ajouter un étudiant)
    @PostMapping
    public Etudiant createEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.saveEtudiant(etudiant);
    }

    // DELETE : http://localhost:8080/api/etudiants/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.noContent().build();
    }
}