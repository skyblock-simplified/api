package dev.sbs.sbsapi;

import dev.sbs.sbsapi.response.SkyBlockEmojis;
import dev.sbs.sbsapi.response.SkyBlockImages;
import dev.sbs.sbsapi.response.SkyBlockItems;
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
public final class SbsApiGsonContributor implements GsonContributor {

    @Override
    public void contribute(GsonSettings.@NotNull Builder builder) {
        builder
            .withTypeAdapter(SkyBlockEmojis.class, new SkyBlockEmojis.Deserializer())
            .withTypeAdapter(SkyBlockImages.class, new SkyBlockImages.Deserializer())
            .withTypeAdapter(SkyBlockItems.class, new SkyBlockItems.Deserializer());
    }

}
