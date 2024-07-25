package com.quantum_pixel.arg.utilities;

import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.function.Supplier;

public class Retry<T> {
    private int retryInterval = 500;
    private int totalRetryNumber = 3;
    private Class<? extends Exception>[] retryOn;
    private int initialDelay = 0;

    public Retry() {
    }

    public static <T> Retry<T> create() {
        return new Retry<>();
    }

    public Retry<T> withRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
        return this;
    }
    public Retry<T> withInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    public Retry<T> withTotalRetryNumber(int totalRetryNumber) {
        this.totalRetryNumber = totalRetryNumber;
        return this;
    }

    @SafeVarargs
    public final Retry<T> withRetryOn(Class<? extends Exception>... retryOn) {
        this.retryOn = retryOn;
        return this;
    }

    @SneakyThrows
    public T execute(Supplier<T> function) throws RuntimeException {
        int attempt = 0;
        while (true) {
            try {
                if(initialDelay > 0 && attempt == 0) {
                    Thread.sleep(initialDelay);
                }
                return function.get();
            } catch (Exception ex) {
                attempt++;
                boolean noMoreAttempts = attempt >= totalRetryNumber;
                if (noMoreAttempts || !shouldRetryOnException(ex)) {
                    throw ex;
                }
                Thread.sleep(retryInterval);
            }
        }
    }

    private boolean shouldRetryOnException(Exception ex) {
       return Arrays.stream(retryOn)
               .filter(el-> el.isInstance(ex))
               .map(el-> true)
               .findFirst()
               .orElse(false);
    }
}