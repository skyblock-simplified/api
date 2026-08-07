package dev.sbs.simplifiedapi.exception;

import com.google.gson.Gson;
import dev.simplified.client.exception.JsonApiException;
import org.jetbrains.annotations.NotNull;

public final class SimplifiedApiException extends JsonApiException {

    public SimplifiedApiException(@NotNull Gson gson, @NotNull String methodKey, @NotNull feign.Response response) {
        super(methodKey, response, "SBS");
        this.resolve(gson, SimplifiedErrorResponse.class);
    }

    @Override
    public @NotNull SimplifiedErrorResponse getResponse() {
        return (SimplifiedErrorResponse) super.getResponse();
    }

}
