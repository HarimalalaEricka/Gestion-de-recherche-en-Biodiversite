package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "mediaid")

    private Integer mediaID;

    @Column(name = "accessuri", nullable = false)

    private String accessURI;

    @Column(name = "associatedobservationreference")

    private String associatedObservationReference;

    @Column(name = "caption")

    private String caption;

    @Column(name = "capturedevice")

    private String captureDevice;

    @Column(name = "comments")

    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid")          
    private Occurrence occurrence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxonid")
    private Taxon taxon;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_media", nullable = false)

    private TypeMedia typeMedia;

    public Integer getMediaID() { return mediaID; }

    public void setMediaID(Integer mediaID) { this.mediaID = mediaID; }

    public String getAccessURI() { return accessURI; }

    public void setAccessURI(String accessURI) { this.accessURI = accessURI; }

    public String getAssociatedObservationReference() { return associatedObservationReference; }

    public void setAssociatedObservationReference(String associatedObservationReference) { this.associatedObservationReference = associatedObservationReference; }

    public String getCaption() { return caption; }

    public void setCaption(String caption) { this.caption = caption; }

    public String getCaptureDevice() { return captureDevice; }

    public void setCaptureDevice(String captureDevice) { this.captureDevice = captureDevice; }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public Occurrence getOccurrence() { return occurrence; }

    public void setOccurrence(Occurrence occurrence) { this.occurrence = occurrence; }

    public Taxon getTaxon() { return taxon; }

    public void setTaxon(Taxon taxon) { this.taxon = taxon; }

    public TypeMedia getTypeMedia() { return typeMedia; }

    public void setTypeMedia(TypeMedia typeMedia) { this.typeMedia = typeMedia; }

    @PrePersist
    @PreUpdate
    private void verifierRattachement() {
        if ((occurrence == null) == (taxon == null)) {
            throw new IllegalStateException(
                "Un média doit être rattaché à une occurrence ou à un taxon, pas aux deux");
        }
    }
}
