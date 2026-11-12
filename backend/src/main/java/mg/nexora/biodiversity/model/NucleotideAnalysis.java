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
    private Identification identification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nucleotidesequenceid", nullable = false)
    private NucleotideSequence nucleotideSequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "molecularprotocolid", nullable = false)
    private MolecularProtocol molecularProtocol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materialentityid", nullable = false)
    private MaterialEntity materialEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid", nullable = false)
    private Event event;

    public Integer getNucleotideAnalysisID() { return nucleotideAnalysisID; }

    public void setNucleotideAnalysisID(Integer nucleotideAnalysisID) { this.nucleotideAnalysisID = nucleotideAnalysisID; }

    public Long getReadCount() { return readCount; }

    public void setReadCount(Long readCount) { this.readCount = readCount; }

    public Long getTotalReadCount() { return totalReadCount; }

    public void setTotalReadCount(Long totalReadCount) { this.totalReadCount = totalReadCount; }

    public Identification getIdentification() { return identification; }

    public void setIdentification(Identification identification) { this.identification = identification; }

    public NucleotideSequence getNucleotideSequence() { return nucleotideSequence; }

    public void setNucleotideSequence(NucleotideSequence nucleotideSequence) { this.nucleotideSequence = nucleotideSequence; }

    public MolecularProtocol getMolecularProtocol() { return molecularProtocol; }

    public void setMolecularProtocol(MolecularProtocol molecularProtocol) { this.molecularProtocol = molecularProtocol; }

    public MaterialEntity getMaterialEntity() { return materialEntity; }

    public void setMaterialEntity(MaterialEntity materialEntity) { this.materialEntity = materialEntity; }

    public Event getEvent() { return event; }

    public void setEvent(Event event) { this.event = event; }

}
