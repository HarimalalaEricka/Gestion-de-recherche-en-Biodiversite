package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "materialentitytype")
public class MaterialEntityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "materialentitytypeid")

    private Integer materialEntityTypeID;

    @Column(name = "type")

    private String type;

    public Integer getMaterialEntityTypeID() { return materialEntityTypeID; }

    public void setMaterialEntityTypeID(Integer materialEntityTypeID) { this.materialEntityTypeID = materialEntityTypeID; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

}
