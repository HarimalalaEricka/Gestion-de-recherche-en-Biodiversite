package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "protocol")
public class Protocol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "protocolid")

    private Integer protocolID;

    @Column(name = "protocolname")

    private String protocolName;

    @Column(name = "protocoldescription")

    private String protocolDescription;

    @Column(name = "protocolreferences")

    private String protocolReferences;

    @Column(name = "protocolremarks")

    private String protocolRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protocoltypeid", nullable = false)
    private ProtocolType protocolTypeID;

    public Integer getProtocolID() { return protocolID; }

    public void setProtocolID(Integer protocolID) { this.protocolID = protocolID; }

    public String getProtocolName() { return protocolName; }

    public void setProtocolName(String protocolName) { this.protocolName = protocolName; }

    public String getProtocolDescription() { return protocolDescription; }

    public void setProtocolDescription(String protocolDescription) { this.protocolDescription = protocolDescription; }

    public String getProtocolReferences() { return protocolReferences; }

    public void setProtocolReferences(String protocolReferences) { this.protocolReferences = protocolReferences; }

    public String getProtocolRemarks() { return protocolRemarks; }

    public void setProtocolRemarks(String protocolRemarks) { this.protocolRemarks = protocolRemarks; }

    public ProtocolType getProtocolTypeID() { return protocolTypeID; }

    public void setProtocolTypeID(ProtocolType protocolTypeID) { this.protocolTypeID = protocolTypeID; }

}
