package dev.siea.unifi4j.async;

import dev.siea.unifi4j.exception.UnifiException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.util.function.Function;

public class UnifiAction<T> {
    private final CompletableFuture<T> future;

    public UnifiAction(CompletableFuture<T> future) {
        this.future = future;
    }

    /** Returns the underlying future for advanced composition. */
    public CompletableFuture<T> toFuture() {
        return future;
    }

    public void queue(Consumer<? super T> success, Consumer<Throwable> failure) {
        future.whenComplete((result, throwable) -> {
            if (throwable != null) failure.accept(unwrap(throwable));
            else success.accept(result);
        });
    }

    public T complete() throws UnifiException {
        try {
            return future.join();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof UnifiException) throw (UnifiException) cause;
            throw new UnifiException("Unexpected error during request", cause);
        }
    }

    public <U> UnifiAction<U> thenApply(Function<? super T, ? extends U> fn) {
        return new UnifiAction<>(future.thenApply(fn));
    }

    /** Chains another async action (e.g. use result of getInfo() to call getSites()). */
    public <U> UnifiAction<U> thenCompose(Function<? super T, ? extends UnifiAction<U>> fn) {
        return new UnifiAction<>(future.thenCompose(t -> fn.apply(t).toFuture()));
    }

    private static Throwable unwrap(Throwable throwable) {
        if (throwable instanceof CompletionException && throwable.getCause() != null)
            return throwable.getCause();
        return throwable;
    }
}
