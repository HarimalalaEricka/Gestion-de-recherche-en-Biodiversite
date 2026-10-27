package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "nucleotideanalysis")
public class NucleotideAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "nucleotideanalysisid")

    private Integer nucleotideAnalysisID;

    @Column(name = "readcount_")

    private Long readCount;

    @Column(name = "totalreadcount_")

    private Long totalReadCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identificationid")
    private Identification identificationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nucleotidesequenceid", nullable = false)
    private NucleotideSequence nucleotideSequenceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "molecularprotocolid", nullable = false)
    private MolecularProtocol molecularProtocolID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materialentityid", nullable = false)
    private MaterialEntity materialEntityID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid", nullable = false)
    private Event eventID;

    public Integer getNucleotideAnalysisID() { return nucleotideAnalysisID; }

    public void setNucleotideAnalysisID(Integer nucleotideAnalysisID) { this.nucleotideAnalysisID = nucleotideAnalysisID; }

    public Long getReadCount() { return readCount; }

    public void setReadCount(Long readCount) { this.readCount = readCount; }

    public Long getTotalReadCount() { return totalReadCount; }

    public void setTotalReadCount(Long totalReadCount) { this.totalReadCount = totalReadCount; }

    public Identification getIdentificationID() { return identificationID; }

    public void setIdentificationID(Identification identificationID) { this.identificationID = identificationID; }

    public NucleotideSequence getNucleotideSequenceID() { return nucleotideSequenceID; }

    public void setNucleotideSequenceID(NucleotideSequence nucleotideSequenceID) { this.nucleotideSequenceID = nucleotideSequenceID; }

    public MolecularProtocol getMolecularProtocolID() { return molecularProtocolID; }

    public void setMolecularProtocolID(MolecularProtocol molecularProtocolID) { this.molecularProtocolID = molecularProtocolID; }

    public MaterialEntity getMaterialEntityID() { return materialEntityID; }

    public void setMaterialEntityID(MaterialEntity materialEntityID) { this.materialEntityID = materialEntityID; }

    public Event getEventID() { return eventID; }

    public void setEventID(Event eventID) { this.eventID = eventID; }

}
