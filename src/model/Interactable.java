package model;

import _59frames.ds.lando.model.Command;

import java.util.List;

/**
 * {@link Interactable}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */

@FunctionalInterface
public interface Interactable {
    List<Command> getInteractableCommands();
}
