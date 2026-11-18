package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.repository.MeasurementOrFactRepository;
import mg.nexora.biodiversity.repository.OccurrenceRepository;
import mg.nexora.biodiversity.repository.AgentRechercheRepository;
import mg.nexora.biodiversity.repository.MeasurementTypeRepository;
import mg.nexora.biodiversity.repository.MeasurementUnitRepository;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.model.MeasurementOrFact;
import mg.nexora.biodiversity.repository.MeasurementOrFactRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class MeasurementOrFactService {
    private final MeasurementOrFactRepository repository;
    private final OccurrenceRepository occurrenceRepository;
    private final AgentRechercheRepository agentRechercheRepository;
    private final MeasurementTypeRepository typeRepository;
    private final MeasurementUnitRepository unitRepository;

    public MeasurementOrFactService(MeasurementOrFactRepository repository, OccurrenceRepository occurrenceRepository, AgentRechercheRepository agentRechercheRepository, MeasurementTypeRepository typeRepository, MeasurementUnitRepository unitRepository) {
        this.repository = repository;
        this.occurrenceRepository = occurrenceRepository;
        this.agentRechercheRepository = agentRechercheRepository;
        this.typeRepository = typeRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional(readOnly = true)
    public List<MeasurementOrFact> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MeasurementOrFact> findById(Integer id) {
        return repository.findById(id);
    }

    public MeasurementOrFact save(MeasurementOrFact entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<MeasurementOrFact> partialUpdate(Integer id, MeasurementOrFact entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getMeasurementValue() != null) existing.setMeasurementValue(entity.getMeasurementValue());
                    if (entity.getMeasurementAccuracy() != null) existing.setMeasurementAccuracy(entity.getMeasurementAccuracy());
                    if (entity.getMeasurementMethod() != null) existing.setMeasurementMethod(entity.getMeasurementMethod());
                    if (entity.getMeasurementDeterminedDate() != null) existing.setMeasurementDeterminedDate(entity.getMeasurementDeterminedDate());
                    if (entity.getMeasurementRemarks() != null) existing.setMeasurementRemarks(entity.getMeasurementRemarks());
                    if (entity.getOccurrence() != null) existing.setOccurrence(entity.getOccurrence());
                    if (entity.getMeasurementOrFact1() != null) existing.setMeasurementOrFact1(entity.getMeasurementOrFact1());
                    if (entity.getMeasurementUnit() != null) existing.setMeasurementUnit(entity.getMeasurementUnit());
                    if (entity.getMeasurementType() != null) existing.setMeasurementType(entity.getMeasurementType());
                    return repository.save(existing);
                });
    }

    @Transactional
    public MeasurementOrFact create(MeasurementOrFact m, Integer utilisateurId) {

        // Jamais d'identifiant venant du client : on veut une insertion, pas une mise à jour
        m.setMeasurementOrFactID(null);

        if (m.getOccurrence() == null || m.getOccurrence().getOccurrenceID() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Occurrence manquante");
        }

        System.out.println("occurrenceID = " + m.getOccurrence().getOccurrenceID());
        Occurrence occurrence = occurrenceRepository.findById(m.getOccurrence().getOccurrenceID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Occurrence introuvable"));

        // occurrence -> événement -> dataset -> recherche
        Integer rechercheId = occurrence.getEvent().getDataset().getRecherche().getRechercheID();
        if (!agentRechercheRepository.estParticipant(rechercheId, utilisateurId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne participez pas à cette recherche");
        }

        m.setOccurrence(occurrence);
        m.setMeasurementType(resolveType(m.getMeasurementType()));
        m.setMeasurementUnit(resolveUnit(m.getMeasurementUnit()));

        m.setMeasurementValue(m.getMeasurementValue().trim());
        m.setMeasurementMethod(blankToNull(m.getMeasurementMethod()));
        m.setMeasurementAccuracy(blankToNull(m.getMeasurementAccuracy()));
        // m.setMeasurementDeterminedBy(blankToNull(m.getMeasurementDeterminedBy()));
        m.setMeasurementRemarks(blankToNull(m.getMeasurementRemarks()));

        return repository.saveAndFlush(m);
    }

    @Transactional(readOnly = true)
    public Page<MeasurementOrFact> listByOccurrence(Integer occurrenceID, Pageable pageable) {
        return repository.findByOccurrence_OccurrenceID(occurrenceID, pageable);
    }

    /* ---------- type et unité : existant ou création ---------- */

    private MeasurementType resolveType(MeasurementType recu) {
        if (recu.getMeasurementTypeID() != null) {
            return typeRepository.findById(recu.getMeasurementTypeID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Type de mesure introuvable"));
        }

        String nom = normaliser(recu.getMeasurementType());
        if (nom == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de mesure manquant");
        }

        // même nom (sans tenir compte de la casse) : on réutilise l'existant
        return typeRepository.findByMeasurementTypeIgnoreCase(nom).orElseGet(() -> {
            MeasurementType t = new MeasurementType();   // nouvel objet : on ne persiste pas celui du client
            t.setMeasurementType(nom);
            return typeRepository.save(t);
        });
    }

    private MeasurementUnit resolveUnit(MeasurementUnit recu) {
        if (recu.getMeasurementUnitID() != null) {
            return unitRepository.findById(recu.getMeasurementUnitID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unité de mesure introuvable"));
        }

        String nom = normaliser(recu.getMeasurementUnit());
        if (nom == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unité de mesure manquante");
        }

        return unitRepository.findByMeasurementUnitIgnoreCase(nom).orElseGet(() -> {
            MeasurementUnit u = new MeasurementUnit();
            u.setMeasurementUnit(nom);
            return unitRepository.save(u);
        });
    }

    // trim + espaces multiples réduits à un seul ; null si vide
    private static String normaliser(String s) {
        if (s == null) return null;
        String t = s.trim().replaceAll("\\s+", " ");
        return t.isEmpty() ? null : t;
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
