import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessTimerTest {

    private static final long FOUR_SECONDS = 4000;
    private static final long HALF_SECOND = 500;

    private static final long MARGIN = 50;

    void sleep(long milliseconds) {

        long currentMillis = System.currentTimeMillis();

        do {
            // Nothing
        } while (System.currentTimeMillis() - currentMillis < milliseconds);
    }

    @Test
    void timing_method_really_returns_the_execution_time() {

        ProcessTimer timer = new ProcessTimer();
        timer.time(() -> sleep(FOUR_SECONDS));

        assertTrue(timer.getDuration().toMillis() >= FOUR_SECONDS);
        assertTrue(timer.getDuration().toMillis() <= FOUR_SECONDS + MARGIN);
    }

    @Test
    void onStart_only_gets_called_once() {

        ProcessTimer timer = new ProcessTimer();
        AtomicInteger callCounter = new AtomicInteger();

        timer.onStart(ourTimer -> callCounter.getAndIncrement());
        timer.time(() -> sleep(HALF_SECOND));

        assertEquals(1, callCounter.get());

        assertTrue(timer.getDuration().toMillis() >= HALF_SECOND);
        assertTrue(timer.getDuration().toMillis() <= HALF_SECOND + MARGIN);
    }

    @Test
    void onStop_only_gets_called_once() {

        ProcessTimer timer = new ProcessTimer();
        AtomicInteger callCounter = new AtomicInteger();

        timer.onStart(ourTimer -> callCounter.getAndIncrement());
        timer.time(() -> sleep(HALF_SECOND));

        assertEquals(1, callCounter.get());
        assertTrue(timer.getDuration().toMillis() >= HALF_SECOND);
        assertTrue(timer.getDuration().toMillis() <= HALF_SECOND + MARGIN);
    }

    @Test
    void onStart_does_not_increase_timer_duration() {

        ProcessTimer timer = new ProcessTimer();

        timer.onStart(ourTimer -> sleep(HALF_SECOND));
        timer.time(() -> sleep(HALF_SECOND));

        assertTrue(timer.getDuration().toMillis() >= HALF_SECOND);
        assertTrue(timer.getDuration().toMillis() <= HALF_SECOND + MARGIN);
    }

    @Test
    void onStop_does_not_increase_timer_duration() {

        ProcessTimer timer = new ProcessTimer();

        timer.onStop(ourTimer -> sleep(HALF_SECOND));
        timer.time(() -> sleep(HALF_SECOND));

        assertTrue(timer.getDuration().toMillis() >= HALF_SECOND);
        assertTrue(timer.getDuration().toMillis() <= HALF_SECOND + MARGIN);
    }

    @Test
    void time_returns_supplied_value() {

        ProcessTimer timer = new ProcessTimer();
        boolean result = timer.time(() -> true);

        assertTrue(result);
    }

    @Test
    void onStart_and_onStop_can_be_chained() {

        AtomicInteger startCounter = new AtomicInteger();
        AtomicInteger stopCounter = new AtomicInteger();

        boolean result = new ProcessTimer()
                .onStart(ourTimer -> startCounter.getAndIncrement())
                .onStop(ourTimer -> stopCounter.getAndIncrement())
                .time(() -> true);

        assertTrue(result);
        assertEquals(1, startCounter.get());
        assertEquals(1, stopCounter.get());
    }
}
