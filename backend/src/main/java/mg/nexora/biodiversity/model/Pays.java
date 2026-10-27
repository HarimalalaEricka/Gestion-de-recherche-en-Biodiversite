package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "pays")
public class Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "paysid")

    private Integer paysID;

    @Column(name = "pays")

    private String pays;

    public Integer getPaysID() { return paysID; }

    public void setPaysID(Integer paysID) { this.paysID = paysID; }

    public String getPays() { return pays; }

    public void setPays(String pays) { this.pays = pays; }

}
