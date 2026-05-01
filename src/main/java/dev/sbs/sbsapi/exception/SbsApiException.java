package dev.sbs.sbsapi.exception;

import com.google.gson.Gson;
import dev.simplified.client.exception.JsonApiException;
import org.jetbrains.annotations.NotNull;

public final class SbsApiException extends JsonApiException {

    public SbsApiException(@NotNull Gson gson, @NotNull String methodKey, @NotNull feign.Response response) {
        super(methodKey, response, "SBS");
        this.resolve(gson, SbsErrorResponse.class);
    }

    @Override
    public @NotNull SbsErrorResponse getResponse() {
        return (SbsErrorResponse) super.getResponse();
    }

}
