package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.repository.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class MediaService {

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
    private final OccurrenceRepository occurrenceRepository;
    private final AgentRechercheRepository agentRechercheRepository;
    private final MediaRepository repository;
    private final FileStorageService storageService;

    public MediaService(MediaRepository repository,FileStorageService storageService, OccurrenceRepository occurrenceRepository, AgentRechercheRepository agentRechercheRepository) {
        this.repository = repository;
        this.storageService = storageService;
        this.occurrenceRepository = occurrenceRepository;
        this.agentRechercheRepository = agentRechercheRepository;
    }

    @Transactional(readOnly = true)
    public Page<Media> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Media>from(params), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Media> findById(Integer id) {
        return repository.findById(id);
    }

    public Media save(Media entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Media> partialUpdate(Integer id, Media entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getAccessURI() != null) existing.setAccessURI(entity.getAccessURI());
                    if (entity.getAssociatedObservationReference() != null) existing.setAssociatedObservationReference(entity.getAssociatedObservationReference());
                    if (entity.getCaption() != null) existing.setCaption(entity.getCaption());
                    if (entity.getCaptureDevice() != null) existing.setCaptureDevice(entity.getCaptureDevice());
                    if (entity.getComments() != null) existing.setComments(entity.getComments());
                    if (entity.getOccurrence() != null) existing.setOccurrence(entity.getOccurrence());
                    return repository.save(existing);
                });
    }

    @Transactional
    public Media upload(MultipartFile file, Integer occurrenceId,
                        String caption, String comments,
                        String captureDevice, String associatedObservationReference,
                        Integer utilisateurId) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fichier vide");
        }

        TypeMedia typeMedia = TYPES_AUTORISES.get(file.getContentType());
        if (typeMedia == null) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Type de fichier non autorisé");
        }

        Occurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Occurrence introuvable"));

        // occurrence -> événement -> dataset -> recherche
        Integer rechercheId = occurrence.getEvent().getDataset().getRecherche().getRechercheID();

        if (!agentRechercheRepository.estParticipant(rechercheId, utilisateurId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne participez pas à cette recherche");
        }

        String objectName = storageService.store(file, "occurrences/" + occurrenceId);

        try {
            Media m = new Media();
            m.setAccessURI(storageService.publicUrl(objectName));
            m.setCaption(blankToNull(caption));
            m.setComments(blankToNull(comments));
            m.setCaptureDevice(blankToNull(captureDevice));
            m.setAssociatedObservationReference(blankToNull(associatedObservationReference));
            m.setTypeMedia(typeMedia);
            m.setOccurrence(occurrence);
            return repository.saveAndFlush(m);
        } catch (RuntimeException e) {
            storageService.delete(objectName);   // évite un fichier orphelin si la base échoue
            throw e;
        }
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
