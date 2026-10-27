package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class IdentificationReferenceKey implements Serializable {
    private Integer identificationID;
    private Integer referenceID;

    public IdentificationReferenceKey() {}

    public Integer getIdentificationID() { return identificationID; }
    public void setIdentificationID(Integer identificationID) { this.identificationID = identificationID; }
    public Integer getReferenceID() { return referenceID; }
    public void setReferenceID(Integer referenceID) { this.referenceID = referenceID; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentificationReferenceKey that = (IdentificationReferenceKey) o;
        return Objects.equals(identificationID, that.identificationID) && Objects.equals(referenceID, that.referenceID);
    }
    @Override public int hashCode() { return Objects.hash(identificationID, referenceID); }
}
