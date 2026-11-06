package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.repository.*;
import mg.nexora.biodiversity.dto.CreerRechercheRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class RechercheService {
    private final RechercheRepository repository;
    private final UtilisateurRepository utilisateurRepository;
    private final AgentRepository agentRepository;
    private final AgentRechercheRepository agentRechercheRepository;



    public RechercheService(RechercheRepository repository, UtilisateurRepository utilisateurRepository, AgentRepository agentRepository, AgentRechercheRepository agentRechercheRepository) {
        this.repository = repository;
        this.utilisateurRepository = utilisateurRepository;
        this.agentRepository = agentRepository;
        this.agentRechercheRepository = agentRechercheRepository;
    }

    @Transactional(readOnly = true)
    public Page<Recherche> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Recherche>from(params), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Recherche> findById(Integer id) {
        return repository.findById(id);
    }

    public Recherche save(Recherche entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Recherche> partialUpdate(Integer id, Recherche entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getSujet() != null) existing.setSujet(entity.getSujet());
                    if (entity.getDescription() != null) existing.setDescription(entity.getDescription());
                    if (entity.getDateDebut() != null) existing.setDateDebut(entity.getDateDebut());
                    if (entity.getDateCreation() != null) existing.setDateCreation(entity.getDateCreation());
                    if (entity.getDateFin() != null) existing.setDateFin(entity.getDateFin());
                    if (entity.getNotePerso() != null) existing.setNotePerso(entity.getNotePerso());
                    if (entity.getConfidentialite() != null) existing.setConfidentialite(entity.getConfidentialite());
                    return repository.save(existing);
                });
    }

    @Transactional
    public Recherche creerAvecAgent(CreerRechercheRequest req, String emailConnecte) {
        Utilisateur user = utilisateurRepository.findByEmail(emailConnecte)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Recherche r = new Recherche();
        r.setSujet(req.sujet());
        r.setDescription(req.description());
        r.setDateDebut(req.dateDebut());
        r.setDateFin(req.dateFin());
        r.setConfidentialite(req.confidentialite());
        r.setDateCreation(LocalDateTime.now());
        r = repository.save(r);

        // Réutilise l'agent de l'utilisateur s'il existe déjà, sinon le crée
        Agent agent = agentRepository.findByUtilisateurIdAndOrderRole(user.getUtilisateurID(), 1)
            .orElseGet(() -> {
                Agent a = new Agent();
                a.setUtilisateurId(user.getUtilisateurID());
                a.setOrderRole(1);
                a = agentRepository.save(a);
                return a;
            });


        AgentRecherche ar = new AgentRecherche();
        ar.setAgent(agent);
        ar.setRecherche(r);
        agentRechercheRepository.save(ar);

        return r;
    }
    @Transactional(readOnly = true)
    public Page<Recherche> findByUtilisateur(Integer utilisateurId,
                                            Confidentiality confidentialite,
                                            Pageable pageable) {
        return confidentialite == null
                ? repository.findByUtilisateurId(utilisateurId, pageable)
                : repository.findByUtilisateurIdAndConfidentialite(utilisateurId, confidentialite, pageable);
    }
}
