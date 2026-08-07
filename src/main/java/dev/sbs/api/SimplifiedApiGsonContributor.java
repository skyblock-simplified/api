package dev.sbs.api;

import dev.sbs.api.response.SkyBlockEmojis;
import dev.sbs.api.response.SkyBlockImages;
import dev.sbs.api.response.SkyBlockItems;
import dev.simplified.gson.GsonContributor;
import dev.simplified.gson.GsonSettings;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

/**
 * Registers the SBS API response deserializers with {@link GsonSettings#defaults()}.
 * <p>
 * Discovered via the {@link ServiceLoader} entry at
 * {@code META-INF/services/dev.simplified.gson.GsonContributor} whenever this
 * module is on the classpath; consumers of {@code GsonSettings.defaults()} get
 * the adapters automatically without touching their own bootstrap code.
 */
public final class SimplifiedApiGsonContributor implements GsonContributor {

    @Override
    public void contribute(GsonSettings.@NotNull Builder builder) {
        builder
            .withTypeAdapter(SkyBlockEmojis.class, new SkyBlockEmojis.Deserializer())
            .withTypeAdapter(SkyBlockImages.class, new SkyBlockImages.Deserializer())
            .withTypeAdapter(SkyBlockItems.class, new SkyBlockItems.Deserializer());
    }

}
