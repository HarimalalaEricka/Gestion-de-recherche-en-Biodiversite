package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "taxon")
public class Taxon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "taxonid")

    private Integer taxonID;

    @Column(name = "scientificname", nullable = false)

    private String scientificName;

    @Column(name = "kingdom")

    private String kingdom;

    @Column(name = "phylum")

    private String phylum;

    @Column(name = "class")
    private String taxonomicClass;

    @Column(name = "order_")

    private String order;

    @Column(name = "family")

    private String family;

    @Column(name = "genus")

    private String genus;

    @Column(name = "specificepithet")

    private String specificEpithet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxonrankid", nullable = false)
    private TaxonRank taxonRankID;

    public Integer getTaxonID() { return taxonID; }

    public void setTaxonID(Integer taxonID) { this.taxonID = taxonID; }

    public String getScientificName() { return scientificName; }

    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getKingdom() { return kingdom; }

    public void setKingdom(String kingdom) { this.kingdom = kingdom; }

    public String getPhylum() { return phylum; }

    public void setPhylum(String phylum) { this.phylum = phylum; }

    public String getTaxonomicClass() {
        return taxonomicClass;
    }

    public void setTaxonomicClass(String taxonomicClass) {
        this.taxonomicClass = taxonomicClass;
    }

    public String getOrder() { return order; }

    public void setOrder(String order) { this.order = order; }

    public String getFamily() { return family; }

    public void setFamily(String family) { this.family = family; }

    public String getGenus() { return genus; }

    public void setGenus(String genus) { this.genus = genus; }

    public String getSpecificEpithet() { return specificEpithet; }

    public void setSpecificEpithet(String specificEpithet) { this.specificEpithet = specificEpithet; }

    public TaxonRank getTaxonRankID() { return taxonRankID; }

    public void setTaxonRankID(TaxonRank taxonRankID) { this.taxonRankID = taxonRankID; }

}
