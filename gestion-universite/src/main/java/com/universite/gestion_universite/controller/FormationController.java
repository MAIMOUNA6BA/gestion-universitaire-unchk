package com.universite.gestion_universite.controller;

import com.universite.gestion_universite.model.Formation;
import com.universite.gestion_universite.service.FormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/formations")
@CrossOrigin(origins = "*")
public class FormationController {
    @Autowired
    private FormationService formationService;

    @GetMapping
    public List<Formation> getAll() { return formationService.getAllFormations(); }

    @PostMapping
    public Formation create(@RequestBody Formation f) { return formationService.saveFormation(f); }
}