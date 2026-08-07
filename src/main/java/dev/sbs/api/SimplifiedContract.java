package dev.sbs.api;

import api.simplified.mojang.response.MojangProfile;
import api.simplified.mojang.response.MojangUsername;
import dev.sbs.api.exception.SimplifiedApiException;
import dev.sbs.api.response.SkyBlockEmojiData;
import dev.sbs.api.response.SkyBlockEmojis;
import dev.sbs.api.response.SkyBlockImages;
import dev.sbs.api.response.SkyBlockItems;
import dev.simplified.client.request.Contract;
import dev.simplified.client.route.Route;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Feign contract interface for the SBS API, providing cached Mojang profile lookups and
 * SkyBlock asset data (item emojis, images, and metadata).
 *
 * @see <a href="https://api.sbs.dev/">SBS Public API</a>
 */
@Route("api.sbs.dev")
public interface SimplifiedContract extends Contract {

    /**
     * Fetches a Mojang profile by username via the test endpoint.
     *
     * @param username the player username
     * @return the username lookup response
     * @throws SimplifiedApiException if the server responds with an HTTP status of 400 or higher
     * @deprecated use {@link #getProfileFromUsername(String)} instead
     */
    @RequestLine("GET /test/{username}")
    @Deprecated
    @NotNull MojangUsername getTestProfileFromUsername(@NotNull @Param("username") String username) throws SimplifiedApiException;

    /**
     * Fetches a cached Mojang profile by username.
     *
     * @param username the player username
     * @return the Mojang profile with unique id, username, and skin data
     * @throws SimplifiedApiException if the server responds with an HTTP status of 400 or higher
     */
    @RequestLine("GET /mojang/user/{username}")
    @NotNull MojangProfile getProfileFromUsername(@NotNull @Param("username") String username) throws SimplifiedApiException;

    /**
     * Fetches a cached Mojang profile by unique id.
     *
     * @param uniqueId the player's unique id
     * @return the Mojang profile with unique id, username, and skin data
     * @throws SimplifiedApiException if the server responds with an HTTP status of 400 or higher
     */
    @RequestLine("GET /mojang/user/{uniqueId}")
    @NotNull MojangProfile getProfileFromUniqueId(@NotNull @Param("uniqueId") UUID uniqueId) throws SimplifiedApiException;

    /**
     * Fetches the SkyBlock Discord emoji mappings for all items.
     *
     * @return the emoji data keyed by item id
     * @throws SimplifiedApiException if the server responds with an HTTP status of 400 or higher
     */
    @RequestLine("GET /skyblock/emojis.json")
    @NotNull SkyBlockEmojis getEmojis() throws SimplifiedApiException;

    /**
     * Fetches the SkyBlock item image URL mappings.
     *
     * @return the image data keyed by item id
     * @throws SimplifiedApiException if the server responds with an HTTP status of 400 or higher
     */
    @RequestLine("GET /skyblock/images.json")
    @NotNull SkyBlockImages getImages() throws SimplifiedApiException;

    /**
     * Fetches the SkyBlock item metadata.
     *
     * @return the item data keyed by item id
     * @throws SimplifiedApiException if the server responds with an HTTP status of 400 or higher
     */
    @RequestLine("GET /skyblock/items.json")
    @NotNull SkyBlockItems getItems() throws SimplifiedApiException;

    /**
     * Fetches and aggregates all SkyBlock item emoji, image, and metadata into
     * a single composite object.
     *
     * @return the combined emoji data
     * @throws SimplifiedApiException if the server responds with an HTTP status of 400 or higher
     */
    default @NotNull SkyBlockEmojiData getItemEmojis() throws SimplifiedApiException {
        return new SkyBlockEmojiData(
            this.getItems(),
            this.getEmojis(),
            this.getImages()
        );
    }

}
