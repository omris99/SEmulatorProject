package logic.model.argument;

import java.io.Serializable;

public interface Argument extends Serializable {
    String getRepresentation();
    int getIndex();
}
