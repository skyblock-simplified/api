package dev.sbs.api.write;

import com.google.gson.Gson;
import dev.simplified.gson.GsonSettings;
import dev.simplified.persistence.exception.JpaException;
import dev.simplified.persistence.store.WriteRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Covers what survives a trip through the queue.
 *
 * <p>A write request names a live class and holds live rows, which a queue cannot carry, so this is
 * the envelope wrapped around one. What matters is that the far side rebuilds the same instruction:
 * the same type, the same operation and a row equal to the one that was sent.
 */
class WriteEnvelopeTest {

    private static final @NotNull Gson GSON = GsonSettings.defaults().create();

    private static @NotNull QueuedRow row(@NotNull String id, @NotNull String name) {
        QueuedRow row = GSON.fromJson(
            String.format("{\"id\":\"%s\",\"name\":\"%s\"}", id, name),
            QueuedRow.class
        );

        assertThat(row, is(org.hamcrest.Matchers.notNullValue()));
        return row;
    }

    @Test
    @DisplayName("an envelope rebuilds the request it was made from")
    void roundTripsARequest() {
        QueuedRow hub = row("HUB", "Hub");
        WriteEnvelope envelope = WriteEnvelope.of(QueuedRow.class, hub, WriteRequest.Operation.UPSERT, GSON);

        WriteRequest<?> rebuilt = envelope.toRequest(GSON);

        assertThat(rebuilt.type(), equalTo(QueuedRow.class));
        assertThat(rebuilt.operation(), equalTo(WriteRequest.Operation.UPSERT));
        assertThat(rebuilt.rows().size(), is(1));
        assertThat(((QueuedRow) rebuilt.rows().getFirst()).getName(), equalTo("Hub"));
    }

    @Test
    @DisplayName("a delete crosses as a delete")
    void carriesTheOperation() {
        WriteEnvelope envelope = WriteEnvelope.of(
            QueuedRow.class,
            row("HUB", "Hub"),
            WriteRequest.Operation.DELETE,
            GSON
        );

        assertThat(envelope.getOperation(), equalTo(WriteRequest.Operation.DELETE));
        assertThat(envelope.toRequest(GSON).operation(), equalTo(WriteRequest.Operation.DELETE));
    }

    @Test
    @DisplayName("every envelope carries its own identity")
    void identityIsPerEnvelope() {
        QueuedRow hub = row("HUB", "Hub");

        assertThat(
            WriteEnvelope.of(QueuedRow.class, hub, WriteRequest.Operation.UPSERT, GSON).getRequestId()
                .equals(WriteEnvelope.of(QueuedRow.class, hub, WriteRequest.Operation.UPSERT, GSON).getRequestId()),
            is(false)
        );
    }

    @Test
    @DisplayName("a type this process does not have is a refusal naming it")
    void unknownTypeIsNamed() {
        WriteEnvelope envelope = WriteEnvelope.of(
            QueuedRow.class,
            row("HUB", "Hub"),
            WriteRequest.Operation.UPSERT,
            GSON
        );

        // A queue outlives a deployment, so an envelope naming a type this process was not built
        // with has to say which one rather than failing as a cast.
        WriteEnvelope renamed = GSON.fromJson(
            GSON.toJson(envelope).replace(QueuedRow.class.getName(), "dev.sbs.api.write.Nonexistent"),
            WriteEnvelope.class
        );

        JpaException thrown = assertThrows(JpaException.class, renamed::getType);
        assertThat(thrown.getMessage().contains("Nonexistent"), is(true));
    }

}
