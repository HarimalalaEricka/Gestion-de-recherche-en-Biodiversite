package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "locationid")

    private Integer locationID;

    @Column(name = "municipality", nullable = false)

    private String municipality;

    @Column(name = "locality")

    private String locality;

    @Column(name = "decimallatitude")

    private BigDecimal decimalLatitude;

    @Column(name = "decimallongitude")

    private BigDecimal decimalLongitude;

    @Column(name = "geodeticdatum")

    private String geodeticDatum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provinceid", nullable = false)
    private Province provinceID;

    public Integer getLocationID() { return locationID; }

    public void setLocationID(Integer locationID) { this.locationID = locationID; }

    public String getMunicipality() { return municipality; }

    public void setMunicipality(String municipality) { this.municipality = municipality; }

    public String getLocality() { return locality; }

    public void setLocality(String locality) { this.locality = locality; }

    public BigDecimal getDecimalLatitude() { return decimalLatitude; }

    public void setDecimalLatitude(BigDecimal decimalLatitude) { this.decimalLatitude = decimalLatitude; }

    public BigDecimal getDecimalLongitude() { return decimalLongitude; }

    public void setDecimalLongitude(BigDecimal decimalLongitude) { this.decimalLongitude = decimalLongitude; }

    public String getGeodeticDatum() { return geodeticDatum; }

    public void setGeodeticDatum(String geodeticDatum) { this.geodeticDatum = geodeticDatum; }

    public Province getProvinceID() { return provinceID; }

    public void setProvinceID(Province provinceID) { this.provinceID = provinceID; }

}
