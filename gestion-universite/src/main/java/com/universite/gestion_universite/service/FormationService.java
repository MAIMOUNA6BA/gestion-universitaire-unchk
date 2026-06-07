package com.universite.gestion_universite.service;

import com.universite.gestion_universite.model.Formation;
import com.universite.gestion_universite.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FormationService {
    @Autowired
    private FormationRepository formationRepository;

    public List<Formation> getAllFormations() { return formationRepository.findAll(); }
    public Formation saveFormation(Formation f) { return formationRepository.save(f); }
}