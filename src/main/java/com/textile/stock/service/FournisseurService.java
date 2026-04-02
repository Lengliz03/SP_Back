package com.textile.stock.service;

import com.textile.stock.dto.FournisseurDTO;
import com.textile.stock.entity.Fournisseur;
import com.textile.stock.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    public List<FournisseurDTO> findAll() {
        return fournisseurRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FournisseurDTO findById(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));
        return convertToDTO(fournisseur);
    }

    public FournisseurDTO create(FournisseurDTO dto) {
        Fournisseur fournisseur = convertToEntity(dto);
        return convertToDTO(fournisseurRepository.save(fournisseur));
    }

    public FournisseurDTO update(Long id, FournisseurDTO dto) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        fournisseur.setNom(dto.getNom());
        fournisseur.setEmail(dto.getEmail());
        fournisseur.setTelephone(dto.getTelephone());
        fournisseur.setAdresse(dto.getAdresse());
        fournisseur.setPays(dto.getPays());
        fournisseur.setNotes(dto.getNotes());

        return convertToDTO(fournisseurRepository.save(fournisseur));
    }

    public void delete(Long id) {
        fournisseurRepository.deleteById(id);
    }

    private FournisseurDTO convertToDTO(Fournisseur fournisseur) {
        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(fournisseur.getId());
        dto.setNom(fournisseur.getNom());
        dto.setEmail(fournisseur.getEmail());
        dto.setTelephone(fournisseur.getTelephone());
        dto.setAdresse(fournisseur.getAdresse());
        dto.setPays(fournisseur.getPays());
        dto.setNotes(fournisseur.getNotes());
        return dto;
    }

    private Fournisseur convertToEntity(FournisseurDTO dto) {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNom(dto.getNom());
        fournisseur.setEmail(dto.getEmail());
        fournisseur.setTelephone(dto.getTelephone());
        fournisseur.setAdresse(dto.getAdresse());
        fournisseur.setPays(dto.getPays());
        fournisseur.setNotes(dto.getNotes());
        return fournisseur;
    }
}