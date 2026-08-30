package dev.sbs.api.write;

import dev.simplified.annotations.Getter;
import dev.simplified.annotations.Setter;
import dev.simplified.persistence.JpaModel;
import org.jetbrains.annotations.NotNull;

/**
 * A row for exercising the queue envelope.
 *
 * <p>What travels is a type name and a JSON body, so the fixture needs to be a model and needs to
 * bind - nothing about the envelope depends on which model it is.
 */
@Getter
@Setter
public class QueuedRow implements JpaModel {

    /**
     * The row's identifier.
     */
    private @NotNull String id = "";

    /**
     * The row's name, so a round trip is visible as a value rather than only a type.
     */
    private @NotNull String name = "";

}
