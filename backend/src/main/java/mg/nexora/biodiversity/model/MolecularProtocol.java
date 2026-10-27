package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "molecularprotocol")
public class MolecularProtocol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "molecularprotocolid")

    private Integer molecularProtocolID;

    @Column(name = "assaytype")

    private String assayType;

    @Column(name = "molecularprotocolremarks")

    private String molecularProtocolRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protocolid", nullable = false)
    private Protocol protocolID;

    public Integer getMolecularProtocolID() { return molecularProtocolID; }

    public void setMolecularProtocolID(Integer molecularProtocolID) { this.molecularProtocolID = molecularProtocolID; }

    public String getAssayType() { return assayType; }

    public void setAssayType(String assayType) { this.assayType = assayType; }

    public String getMolecularProtocolRemarks() { return molecularProtocolRemarks; }

    public void setMolecularProtocolRemarks(String molecularProtocolRemarks) { this.molecularProtocolRemarks = molecularProtocolRemarks; }

    public Protocol getProtocolID() { return protocolID; }

    public void setProtocolID(Protocol protocolID) { this.protocolID = protocolID; }

}
