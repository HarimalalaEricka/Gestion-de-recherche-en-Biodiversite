package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "eventid")

    private Integer eventID;

    @Column(name = "eventdate")

    private LocalDateTime eventDate;

    @Column(name = "samplingprotocol")

    private String samplingProtocol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protocolid")
    private Protocol protocol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasetid", nullable = false)
    private Dataset dataset;

    public Integer getEventID() { return eventID; }

    public void setEventID(Integer eventID) { this.eventID = eventID; }

    public LocalDateTime getEventDate() { return eventDate; }

    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public String getSamplingProtocol() { return samplingProtocol; }

    public void setSamplingProtocol(String samplingProtocol) { this.samplingProtocol = samplingProtocol; }

    public Protocol getProtocol() { return protocol; }

    public void setProtocol(Protocol protocol) { this.protocol = protocol; }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public Dataset getDataset() { return dataset; }

    public void setDataset(Dataset dataset) { this.dataset = dataset; }

}
