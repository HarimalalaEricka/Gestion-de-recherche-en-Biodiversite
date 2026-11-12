package mg.nexora.biodiversity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "licence")
public class Licence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "licenceid")
    private Integer licenceID;

    @Column(name = "licencename", nullable = false)
    private String licenceName;

    @Column(name = "licencecode", nullable = false, unique = true)
    private String licenceCode;

    @Column(name = "licenceuri")
    private String licenceURI;

    @Column(name = "description")
    private String description;

    @Column(name = "isactive")
    private Boolean isActive = true;

    public Integer getLicenceID() { return licenceID; }

    public void setLicenceID(Integer licenceID) { this.licenceID = licenceID; }

    public String getLicenceName() { return licenceName; }

    public void setLicenceName(String licenceName) { this.licenceName = licenceName; }

    public String getLicenceCode() { return licenceCode; }

    public void setLicenceCode(String licenceCode) { this.licenceCode = licenceCode; }

    public String getLicenceURI() { return licenceURI; }

    public void setLicenceURI(String licenceURI) { this.licenceURI = licenceURI; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Boolean getIsActive() { return isActive; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
