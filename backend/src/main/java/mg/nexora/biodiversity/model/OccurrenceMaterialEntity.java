package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

import mg.nexora.biodiversity.model.keys.OccurrenceMaterialEntityKey;
@Entity
@IdClass(OccurrenceMaterialEntityKey.class)
@Table(name = "occurrence_material_entity")
public class OccurrenceMaterialEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occurrenceid")
    private Occurrence occurrenceID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materialentityid")
    private MaterialEntity materialEntityID;

    public Occurrence getOccurrenceID() { return occurrenceID; }

    public void setOccurrenceID(Occurrence occurrenceID) { this.occurrenceID = occurrenceID; }

    public MaterialEntity getMaterialEntityID() { return materialEntityID; }

    public void setMaterialEntityID(MaterialEntity materialEntityID) { this.materialEntityID = materialEntityID; }

}
