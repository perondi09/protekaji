package perondi.protekaji.exceptions;

import java.time.Instant;

public record ApiError(
        Instant timestamp,
        int status,
        String message
) {
}