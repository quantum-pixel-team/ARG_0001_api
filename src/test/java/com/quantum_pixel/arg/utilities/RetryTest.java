package com.quantum_pixel.arg.utilities;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RetryTest {
    @Test
    public void testRetrySuccessWithoutException() throws Exception {
        Supplier<String> function = mock(Supplier.class);
        when(function.get()).thenReturn("Success");

        Retry<String> retry = Retry.create();
        String result = retry.execute(function);

        assertEquals("Success", result);
        verify(function, times(1)).get();
    }

    @Test
    public void testRetrySuccessAfterException() throws Exception {
        Supplier<String> function = mock(Supplier.class);
        when(function.get())
                .thenThrow(new RuntimeException())
                .thenReturn("Success");

        var retry = Retry.<String>create().withRetryOn(RuntimeException.class).withTotalRetryNumber(2);
        String result = retry.execute(function);

        assertEquals("Success", result);
        verify(function, times(2)).get();
    }

    @Test
    public void testRetryFailureAfterMaxRetries() {
        Supplier<String> function = mock(Supplier.class);
        when(function.get()).thenThrow(new RuntimeException());

        Retry<String> retry = Retry.<String>create().withRetryOn(RuntimeException.class).withTotalRetryNumber(3);

        assertThrows(RuntimeException.class, () -> retry.execute(function));
        verify(function, times(3)).get();
    }

    @Test
    public void testRetryWithInitialDelay() throws Exception {
        Supplier<String> function = mock(Supplier.class);
        when(function.get()).thenReturn("Success");

        Retry<String> retry = Retry.<String>create().withInitialDelay(100).withRetryInterval(50);
        long startTime = System.currentTimeMillis();
        String result = retry.execute(function);
        long endTime = System.currentTimeMillis();

        assertEquals("Success", result);
        assertTrue((endTime - startTime) >= 100);
        verify(function, times(1)).get();
    }

    @Test
    public void testRetryWithRetryInterval() throws Exception {
        Supplier<String> function = mock(Supplier.class);
        when(function.get()).thenThrow(new RuntimeException()).thenReturn("Success");

        Retry<String> retry = Retry.<String>create().withRetryOn(RuntimeException.class).withTotalRetryNumber(2).withRetryInterval(100);
        long startTime = System.currentTimeMillis();
        String result = retry.execute(function);
        long endTime = System.currentTimeMillis();

        assertEquals("Success", result);
        assertTrue((endTime - startTime) >= 100);
        verify(function, times(2)).get();
    }

    @Test
    public void testRetryOnSpecificException() {
        Supplier<String> function = mock(Supplier.class);
        when(function.get()).thenThrow(new IllegalArgumentException());

        Retry<String> retry = Retry.<String>create().withRetryOn(IllegalArgumentException.class).withTotalRetryNumber(3);

        assertThrows(IllegalArgumentException.class, () -> retry.execute(function));
        verify(function, times(3)).get();
    }
}