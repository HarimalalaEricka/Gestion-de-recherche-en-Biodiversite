package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class OccurrenceMaterialEntityKey implements Serializable {
    private Integer occurrence;
    private Integer materialEntity;

    public OccurrenceMaterialEntityKey() {}

    public Integer getOccurrence() { return occurrence; }
    public void setOccurrence(Integer occurrence) { this.occurrence = occurrence; }
    public Integer getMaterialEntity() { return materialEntity; }
    public void setMaterialEntity(Integer materialEntity) { this.materialEntity = materialEntity; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OccurrenceMaterialEntityKey that = (OccurrenceMaterialEntityKey) o;
        return Objects.equals(occurrence, that.occurrence) && Objects.equals(materialEntity, that.materialEntity);
    }
    @Override public int hashCode() { return Objects.hash(occurrence, materialEntity); }
}
