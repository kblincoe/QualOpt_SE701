package org.project36.qualopt.web.rest.errors;

/**
 * Simple Exception to dicate when the Rate Limit has been exceeded
 */
public class GHRateLimitReachedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GHRateLimitReachedException(String message) {
        super(message);
    }
}