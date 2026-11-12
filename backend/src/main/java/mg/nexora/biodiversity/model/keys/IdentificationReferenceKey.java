package mg.nexora.biodiversity.model.keys;

import java.io.Serializable;
import java.util.Objects;

public class IdentificationReferenceKey implements Serializable {
    private Integer identification;
    private Integer reference;

    public IdentificationReferenceKey() {}

    public Integer getIdentification() { return identification; }
    public void setIdentification(Integer identification) { this.identification = identification; }
    public Integer getReference() { return reference; }
    public void setReference(Integer reference) { this.reference = reference; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentificationReferenceKey that = (IdentificationReferenceKey) o;
        return Objects.equals(identification, that.identification) && Objects.equals(reference, that.reference);
    }
    @Override public int hashCode() { return Objects.hash(identification, reference); }
}
