package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "nucleotidesequence")
public class NucleotideSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "nucleotidesequenceid")

    private Integer nucleotideSequenceID;

    @Column(name = "sequence_", nullable = false)

    private String sequence;

    @Column(name = "nucleotidesequenceremarks_")

    private String nucleotideSequenceRemarks;

    public Integer getNucleotideSequenceID() { return nucleotideSequenceID; }

    public void setNucleotideSequenceID(Integer nucleotideSequenceID) { this.nucleotideSequenceID = nucleotideSequenceID; }

    public String getSequence() { return sequence; }

    public void setSequence(String sequence) { this.sequence = sequence; }

    public String getNucleotideSequenceRemarks() { return nucleotideSequenceRemarks; }

    public void setNucleotideSequenceRemarks(String nucleotideSequenceRemarks) { this.nucleotideSequenceRemarks = nucleotideSequenceRemarks; }

}
