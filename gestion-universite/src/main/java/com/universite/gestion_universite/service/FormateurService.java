package com.universite.gestion_universite.service;

import com.universite.gestion_universite.model.Formateur;
import com.universite.gestion_universite.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FormateurService {
    @Autowired
    private FormateurRepository formateurRepository;

    public List<Formateur> getAllFormateurs() { return formateurRepository.findAll(); }
    public Formateur saveFormateur(Formateur f) { return formateurRepository.save(f); }
}