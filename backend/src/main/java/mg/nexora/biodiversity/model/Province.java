package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "province")
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "provinceid")

    private Integer provinceID;

    @Column(name = "province", nullable = false)

    private String province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paysid", nullable = false)
    private Pays pays;

    public Integer getProvinceID() { return provinceID; }

    public void setProvinceID(Integer provinceID) { this.provinceID = provinceID; }

    public String getProvince() { return province; }

    public void setProvince(String province) { this.province = province; }

    public Pays getPays() { return pays; }

    public void setPays(Pays pays) { this.pays = pays; }

}
