package mg.nexora.biodiversity.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import mg.nexora.biodiversity.model.MediaRecherche;
import mg.nexora.biodiversity.model.Recherche;
import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.repository.AgentRechercheRepository;
import mg.nexora.biodiversity.repository.MediaRechercheRepository;
import mg.nexora.biodiversity.repository.RechercheRepository;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
import mg.nexora.biodiversity.util.FilterSpec;

@Service
@Transactional
public class MediaRechercheService {

    // Types MIME acceptés, et le TypeMedia correspondant
    private static final Map<String, TypeMedia> TYPES_AUTORISES = Map.ofEntries(
            // IMAGE
            Map.entry("image/jpeg", TypeMedia.IMAGE),
            Map.entry("image/png", TypeMedia.IMAGE),
            Map.entry("image/gif", TypeMedia.IMAGE),
            Map.entry("image/webp", TypeMedia.IMAGE),
            // VIDEO
            Map.entry("video/mp4", TypeMedia.VIDEO),
            Map.entry("video/webm", TypeMedia.VIDEO),
            Map.entry("video/quicktime", TypeMedia.VIDEO),
            // AUDIO
            Map.entry("audio/mpeg", TypeMedia.AUDIO),
            Map.entry("audio/wav", TypeMedia.AUDIO),
            Map.entry("audio/ogg", TypeMedia.AUDIO),
            Map.entry("audio/mp4", TypeMedia.AUDIO),
            // DOCUMENT
            Map.entry("application/pdf", TypeMedia.DOCUMENT),
            Map.entry("application/msword", TypeMedia.DOCUMENT),
            Map.entry("application/vnd.openxmlformats-officedocument.wordprocessingml.document", TypeMedia.DOCUMENT),
            Map.entry("application/vnd.ms-excel", TypeMedia.DOCUMENT),
            Map.entry("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", TypeMedia.DOCUMENT),
            Map.entry("text/csv", TypeMedia.DOCUMENT),
            Map.entry("text/plain", TypeMedia.DOCUMENT)
    );

    private final MediaRechercheRepository repository;
    private final RechercheRepository rechercheRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AgentRechercheRepository agentRechercheRepository;
    private final FileStorageService storageService;

    public MediaRechercheService(MediaRechercheRepository repository,
                                 RechercheRepository rechercheRepository,
                                 UtilisateurRepository utilisateurRepository,
                                 AgentRechercheRepository agentRechercheRepository,
                                 FileStorageService storageService) {
        this.repository = repository;
        this.rechercheRepository = rechercheRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.agentRechercheRepository = agentRechercheRepository;
        this.storageService = storageService;
    }

    @Transactional(readOnly = true)
    public Page<MediaRecherche> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<MediaRecherche>from(params), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<MediaRecherche> findById(Integer id) {
        return repository.findById(id);
    }

    public MediaRecherche save(MediaRecherche entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Optional<MediaRecherche> partialUpdate(Integer id, MediaRecherche entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getAccessURI() != null) existing.setAccessURI(entity.getAccessURI());
                    if (entity.getCaption() != null) existing.setCaption(entity.getCaption());
                    if (entity.getComments() != null) existing.setComments(entity.getComments());
                    if (entity.getUtilisateur() != null) existing.setUtilisateur(entity.getUtilisateur());
                    if (entity.getRecherche() != null) existing.setRecherche(entity.getRecherche());
                    if (entity.getTypeMedia() != null) existing.setTypeMedia(entity.getTypeMedia());
                    return repository.save(existing);
                });
    }

    @Transactional(readOnly = true)
    public Page<MediaRecherche> findMesMedias(Integer rechercheId, Integer utilisateurId, Pageable pageable) {
        return repository.findMesMedias(rechercheId, utilisateurId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MediaRecherche> findAllMesMedias(Integer rechercheId,
                                                TypeMedia typeMedia,
                                                String q,
                                                Pageable pageable) {

        Specification<MediaRecherche> spec = (root, query, cb) -> cb.conjunction();

        if (rechercheId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("recherche").get("rechercheID"), rechercheId));
        }
        if (typeMedia != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("typeMedia"), typeMedia));
        }
        if (q != null && !q.isBlank()) {
            String pattern = "%" + q.trim().toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> {
                Join<MediaRecherche, Utilisateur> auteur =
                        root.join("utilisateur", JoinType.LEFT);

                return cb.or(
                        cb.like(cb.lower(root.get("caption")), pattern),
                        cb.like(cb.lower(root.get("comments")), pattern),
                        cb.like(cb.lower(auteur.get("nom")), pattern),
                        cb.like(cb.lower(auteur.get("prenom")), pattern)
                );
            });
        }

        return repository.findAll(spec, pageable);
    }

    public MediaRecherche upload(MultipartFile file, Integer rechercheId,
                                 String caption, String comments, Integer utilisateurId, TypeMediaRecherche type) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fichier vide");
        }

        TypeMedia typeMedia = TYPES_AUTORISES.get(file.getContentType());
        if (typeMedia == null) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Type de fichier non autorisé");
        }

        Recherche recherche = rechercheRepository.findById(rechercheId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recherche introuvable"));

        if (!agentRechercheRepository.estParticipant(rechercheId, utilisateurId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne participez pas à cette recherche");
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        String objectName = storageService.store(file, "recherches/" + rechercheId);

        try {
            MediaRecherche m = new MediaRecherche();
            m.setAccessURI(storageService.publicUrl(objectName));
            m.setCaption(caption);
            m.setComments(comments);
            m.setTypeMedia(typeMedia);
            m.setRecherche(recherche);
            m.setUtilisateur(utilisateur);
            m.setTypeMediaRecherche(type);
            return repository.save(m);
        } catch (RuntimeException e) {
            storageService.delete(objectName);   // évite un fichier orphelin si la base échoue
            throw e;
        }
    }
}