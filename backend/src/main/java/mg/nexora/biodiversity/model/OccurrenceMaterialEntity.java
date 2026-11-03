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
    private Occurrence occurrence;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materialentityid")
    private MaterialEntity materialEntity;

    public Occurrence getOccurrence() { return occurrence; }

    public void setOccurrence(Occurrence occurrence) { this.occurrence = occurrence; }

    public MaterialEntity getMaterialEntity() { return materialEntity; }

    public void setMaterialEntity(MaterialEntity materialEntity) { this.materialEntity = materialEntity; }

}
