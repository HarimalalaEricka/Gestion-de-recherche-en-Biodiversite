package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.repository.*;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class IdentificationService {
    private final IdentificationRepository repository;
    private final UtilisateurRepository utilisateurRepository;
    private final OccurrenceRepository occurrenceRepository;
    private final TaxonRepository taxonRepository;
    private final TaxonRankRepository taxonRankRepository;
    private final AgentRepository agentRepository;

    public IdentificationService(IdentificationRepository repository, 
                                UtilisateurRepository utilisateurRepository,
                                 OccurrenceRepository occurrenceRepository,
                                 TaxonRepository taxonRepository,
                                 TaxonRankRepository taxonRankRepository,
                                 AgentRepository agentRepository) {
        this.repository = repository;
        this.utilisateurRepository = utilisateurRepository;
        this.occurrenceRepository = occurrenceRepository;
        this.taxonRepository = taxonRepository;
        this.taxonRankRepository = taxonRankRepository;
        this.agentRepository = agentRepository;
    }

    @Transactional(readOnly = true)
    public Page<Identification> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Identification>from(params), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Identification> findById(Integer id) {
        return repository.findById(id);
    }

    public Identification save(Identification entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Identification> partialUpdate(Integer id, Identification entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getDateIdentified() != null) existing.setDateIdentified(entity.getDateIdentified());
                    if (entity.getIdentificationQualifier() != null) existing.setIdentificationQualifier(entity.getIdentificationQualifier());
                    if (entity.getIsAcceptedIdentification() != null) existing.setIsAcceptedIdentification(entity.getIsAcceptedIdentification());
                    if (entity.getTaxon() != null) existing.setTaxon(entity.getTaxon());
                    if (entity.getAgent() != null) existing.setAgent(entity.getAgent());
                    if (entity.getOccurrence() != null) existing.setOccurrence(entity.getOccurrence());
                    return repository.save(existing);
                });
    }

    @Transactional
    public Identification creerAvecAgent(Identification req, String emailConnecte) {

        Utilisateur user = utilisateurRepository.findByEmail(emailConnecte)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Validation minimale
        if (req.getDateIdentified() == null || req.getTaxon() == null
                || req.getOccurrence() == null || req.getOccurrence().getOccurrenceID() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "dateIdentified, taxon et occurrence sont obligatoires");
        }

        Occurrence occurrence = occurrenceRepository.findById(req.getOccurrence().getOccurrenceID())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Occurrence introuvable"));

        // Taxon : existant (taxonID) ou venant de GBIF (créé ici, dans la même transaction)
        Taxon taxon = resoudreTaxon(req.getTaxon());

        // Réutilise l'agent de l'utilisateur s'il existe déjà, sinon le crée
        Agent agent = agentRepository.findByUtilisateurId(user.getUtilisateurID())
            .orElseGet(() -> {
                Agent a = new Agent();
                a.setUtilisateurId(user.getUtilisateurID());
                return agentRepository.save(a);
            });

        // Nouvelle entité : on ne recopie que les champs autorisés (jamais l'id ni l'agent du client)
        Identification i = new Identification();
        i.setDateIdentified(req.getDateIdentified());
        i.setIdentificationQualifier(req.getIdentificationQualifier());
        i.setIsAcceptedIdentification(req.getIsAcceptedIdentification());
        i.setTaxon(taxon);
        i.setAgent(agent);
        i.setOccurrence(occurrence);

        return repository.save(i);
    }

    private static final Map<String, String> RANGS_FR = Map.of(
        "KINGDOM", "Règne", "PHYLUM", "Embranchement", "CLASS", "Classe",
        "ORDER", "Ordre", "FAMILY", "Famille", "GENUS", "Genre", "SPECIES", "Espèce"
    );

    private Taxon resoudreTaxon(Taxon ref) {

        // 1) Taxon de notre base
        if (ref.getTaxonID() != null) {
            return taxonRepository.findById(ref.getTaxonID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Taxon introuvable"));
        }

        // 2) Taxon GBIF : clé et nom obligatoires
        if (ref.getGbifKey() == null || ref.getScientificName() == null || ref.getScientificName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Taxon invalide : taxonID ou (gbifKey + scientificName) requis");
        }

        // 3) Déjà importé depuis GBIF
        Optional<Taxon> parCle = taxonRepository.findByGbifKey(ref.getGbifKey());
        if (parCle.isPresent()) return parCle.get();

        // 4) Même nom déjà saisi à la main (sans gbifKey) : on le réutilise et on lui ajoute la clé
        Optional<Taxon> parNom = taxonRepository
            .findFirstByScientificNameIgnoreCase(ref.getScientificName().trim());
        if (parNom.isPresent()) {
            Taxon t = parNom.get();
            if (t.getGbifKey() == null) t.setGbifKey(ref.getGbifKey());
            return t;
        }

        // 5) Nouveau taxon
        Taxon t = new Taxon();
        t.setGbifKey(ref.getGbifKey());
        t.setScientificName(ref.getScientificName().trim());
        t.setKingdom(ref.getKingdom());
        t.setPhylum(ref.getPhylum());
        t.setTaxonomicClass(ref.getTaxonomicClass());
        t.setOrder(ref.getOrder());
        t.setFamily(ref.getFamily());
        t.setGenus(ref.getGenus());
        t.setSpecificEpithet(ref.getSpecificEpithet());
        t.setTaxonRank(resoudreRang(ref.getTaxonRank()));
        return taxonRepository.save(t);
    }

    private TaxonRank resoudreRang(TaxonRank ref) {
        if (ref == null || ref.getTaxonRank() == null || ref.getTaxonRank().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rang du taxon manquant");
        }

        String brut = ref.getTaxonRank().trim();

        // Libellé français si on le connaît ("SPECIES" -> "Espèce"), sinon la valeur GBIF telle quelle
        String libelle = RANGS_FR.getOrDefault(brut.toUpperCase(), brut);

        // 1) Déjà présent sous le libellé français ou sous la valeur brute
        return taxonRankRepository.findFirstByTaxonRankIgnoreCase(libelle)
            .or(() -> taxonRankRepository.findFirstByTaxonRankIgnoreCase(brut))
            // 2) Sinon on le crée
            .orElseGet(() -> {
                TaxonRank r = new TaxonRank();
                r.setTaxonRank(libelle);
                return taxonRankRepository.save(r);
            });
    }
}
