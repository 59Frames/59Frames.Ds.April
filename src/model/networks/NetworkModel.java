package model.networks;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;


public interface NetworkModel extends Serializable {
    Matrix forward(@NotNull Matrix input, @NotNull Graph g) throws Exception;

    void resetState();

    List<Matrix> getParameters();
}
