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
    private Protocol protocolID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid", nullable = false)
    private Location locationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasetid", nullable = false)
    private Dataset datasetID;

    public Integer getEventID() { return eventID; }

    public void setEventID(Integer eventID) { this.eventID = eventID; }

    public LocalDateTime getEventDate() { return eventDate; }

    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public String getSamplingProtocol() { return samplingProtocol; }

    public void setSamplingProtocol(String samplingProtocol) { this.samplingProtocol = samplingProtocol; }

    public Protocol getProtocolID() { return protocolID; }

    public void setProtocolID(Protocol protocolID) { this.protocolID = protocolID; }

    public Location getLocationID() { return locationID; }

    public void setLocationID(Location locationID) { this.locationID = locationID; }

    public Dataset getDatasetID() { return datasetID; }

    public void setDatasetID(Dataset datasetID) { this.datasetID = datasetID; }

}
