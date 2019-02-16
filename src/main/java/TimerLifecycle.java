import java.time.Instant;
import java.util.function.Consumer;

public interface TimerLifecycle {

    void onStart(Consumer<ProcessTimer> toExecuteOnStart);
    void onStop(Consumer<ProcessTimer> toExecuteOnEnd);
}
