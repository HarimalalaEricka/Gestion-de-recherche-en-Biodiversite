package mg.nexora.biodiversity.service;

import java.time.Instant;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import mg.nexora.biodiversity.util.*;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.model.keys.AgentRechercheKey;
import mg.nexora.biodiversity.repository.*;

@Service
@Transactional
public class AgentRechercheService {
    private final AgentRechercheRepository repository;
    private final UtilisateurRepository utilisateurRepository;
    private final RechercheRepository rechercheRepository;
    private final AgentRepository agentRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public AgentRechercheService(AgentRechercheRepository repository,
                                 UtilisateurRepository utilisateurRepository,
                                 RechercheRepository rechercheRepository,
                                 AgentRepository agentRepository,
                                 TokenService tokenService,
                                 EmailService emailService) {
        this.repository = repository;
        this.utilisateurRepository = utilisateurRepository;
        this.rechercheRepository = rechercheRepository;
        this.agentRepository = agentRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public Page<AgentRecherche> findAll(Map<String, String> params, Pageable pageable) {
        Map<String, String> filtres = new HashMap<>(params);
        String q = filtres.remove("q"); // traité séparément

        Specification<AgentRecherche> spec = FilterSpec.<AgentRecherche>from(filtres);

        if (q != null && !q.isBlank()) {
            spec = spec.and(rechercheTexte(q.trim().toLowerCase()));
        }

        return repository.findAll(spec, pageable);
    }

    private Specification<AgentRecherche> rechercheTexte(String q) {
        return (root, query, cb) -> {
            String motif = "%" + q + "%";

            Subquery<Integer> sub = query.subquery(Integer.class);
            Root<Utilisateur> u = sub.from(Utilisateur.class);
            sub.select(u.get("utilisateurID")).where(   // nom du champ id dans Utilisateur
                cb.or(
                    cb.like(cb.lower(u.get("nom")), motif),
                    cb.like(cb.lower(u.get("prenom")), motif),
                    cb.like(cb.lower(u.get("email")), motif)
                )
            );

            return root.get("agent").get("utilisateurId").in(sub);
        };
    }

    @Transactional(readOnly = true)
    public Optional<AgentRecherche> findById(AgentRechercheKey id) {
        return repository.findById(id);
    }

    public AgentRecherche save(AgentRecherche entity) {
        return repository.save(entity);
    }

    public void deleteById(AgentRechercheKey id) {
        repository.deleteById(id);
    }

    // ---------- Collaboration ----------

    /** Crée (ou met à jour) la ligne agent_recherche EN_ATTENTE puis envoie le lien par e-mail. */
    public void inviter(Integer rechercheId, String email, RoleCollaboration role, Utilisateur invitant) {
        Recherche recherche = rechercheRepository.findById(rechercheId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recherche introuvable"));

        Utilisateur cible = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun compte avec cet e-mail"));
        if (cible.getStatut() != StatutUser.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce compte n'est pas actif");
        }

        Optional<AgentRecherche> existant =
                repository.trouverParUtilisateurEtRecherche(cible.getUtilisateurID(), rechercheId);

        if (existant.isPresent() && existant.get().getStatut() == StatutCollaboration.ACCEPTEE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette personne collabore déjà à la recherche");
        }

        AgentRecherche ar;
        if (existant.isPresent()) {
            ar = existant.get();                       // ré-invitation : on réutilise la ligne EN_ATTENTE
        } else {
            Agent agent = agentRepository.findByUtilisateurId(cible.getUtilisateurID())
                        .orElseGet(() -> {
                            Agent nouvelAgent = new Agent();
                            nouvelAgent.setUtilisateurId(cible.getUtilisateurID());

                            return agentRepository.save(nouvelAgent);
                        });
            ar = new AgentRecherche();
            ar.setAgent(agent);
            ar.setRecherche(recherche);
        }
        ar.setRoleCollaboration(role);
        ar.setStatut(StatutCollaboration.EN_ATTENTE);
        ar.setInviteParId(invitant.getUtilisateurID());
        ar.setInviteLe(Instant.now());
        repository.save(ar);

        // Le token prouve seulement que l'e-mail appartient à l'invité
        String token = tokenService.creerToken(cible, TypeToken.COLLABORATOR_DEMANDE);
        String lien = frontendUrl + "/collaboration?token=" + token + "&recherche=" + rechercheId;
        emailService.envoyerLien(cible.getEmail(), lien, TypeToken.COLLABORATOR_DEMANDE);
    }

    @Transactional
    public void accepter(
            String token,
            Integer rechercheId,
            String emailAuthentifie) {

        Utilisateur u = tokenService.consommer(
                token,
                TypeToken.COLLABORATOR_DEMANDE,
                emailAuthentifie
        );

        AgentRecherche ar = repository
                .trouverParUtilisateurEtRecherche(
                        u.getUtilisateurID(),
                        rechercheId
                )
                .filter(x -> x.getStatut() == StatutCollaboration.EN_ATTENTE)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Aucune invitation en attente pour cette recherche"
                ));

        ar.setStatut(StatutCollaboration.ACCEPTEE);
        ar.setAccepteLe(Instant.now());

        repository.save(ar);
    }
}