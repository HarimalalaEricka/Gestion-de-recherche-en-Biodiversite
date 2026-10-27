package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class OccurrenceMaterialEntityKey implements Serializable {
    private Integer occurrenceID;
    private Integer materialEntityID;

    public OccurrenceMaterialEntityKey() {}

    public Integer getOccurrenceID() { return occurrenceID; }
    public void setOccurrenceID(Integer occurrenceID) { this.occurrenceID = occurrenceID; }
    public Integer getMaterialEntityID() { return materialEntityID; }
    public void setMaterialEntityID(Integer materialEntityID) { this.materialEntityID = materialEntityID; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OccurrenceMaterialEntityKey that = (OccurrenceMaterialEntityKey) o;
        return Objects.equals(occurrenceID, that.occurrenceID) && Objects.equals(materialEntityID, that.materialEntityID);
    }
    @Override public int hashCode() { return Objects.hash(occurrenceID, materialEntityID); }
}
