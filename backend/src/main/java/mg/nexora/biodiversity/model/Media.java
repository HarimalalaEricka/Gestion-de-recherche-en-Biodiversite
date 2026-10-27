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
    @JoinColumn(name = "occurrenceid", nullable = false)
    private Occurrence occurrenceID;

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

    public Occurrence getOccurrenceID() { return occurrenceID; }

    public void setOccurrenceID(Occurrence occurrenceID) { this.occurrenceID = occurrenceID; }

}
