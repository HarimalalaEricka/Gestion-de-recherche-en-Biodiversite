package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "taxonrank")
public class TaxonRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "taxonrankid")

    private Integer taxonRankID;

    @Column(name = "taxonrank", nullable = false)

    private String taxonRank;

    public Integer getTaxonRankID() { return taxonRankID; }

    public void setTaxonRankID(Integer taxonRankID) { this.taxonRankID = taxonRankID; }

    public String getTaxonRank() { return taxonRank; }

    public void setTaxonRank(String taxonRank) { this.taxonRank = taxonRank; }

}
