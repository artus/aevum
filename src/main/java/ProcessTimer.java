import jdk.nashorn.internal.objects.PrototypeObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter(AccessLevel.PRIVATE)
/**
 * A ProcessTimer times the execution of a Runnable.
 */
public class ProcessTimer implements TimerLifecycle {

    private Consumer<ProcessTimer> onStart;
    private Consumer<ProcessTimer> onStop;

    private Instant start;
    private Instant stop;

    private boolean logStart  = true;
    private boolean logStop = true;

    @Override
    public void onStart(Consumer<ProcessTimer> toExecuteOnStart) {
        this.setOnStart(toExecuteOnStart);
    }

    @Override
    public void onStop(Consumer<ProcessTimer> toExecuteOnEnd) {
        this.setOnStop(toExecuteOnEnd);
    }

    public void start() {
        this.setStart(Instant.now());
        this.getOnStart().accept(this);
    }

    public void stop() {
        this.setStop(Instant.now());
        this.getOnStop().accept(this);
    }

    public <Output> Output time(Supplier<Output> toExecute) {

        this.start();
        Output toReturn = toExecute.get();
        this.stop();

        return toReturn;
    }

    public Duration getDuration() {

        if (!this.isStarted()) throw new IllegalStateException("ProcessTimer not started yet.");
        if (this.getStop() == null) return Duration.between(this.getStart(), Instant.now());
        return Duration.between(this.getStart(), this.getStart());
    }

    public boolean isStarted() {
        return this.getStart() != null;
    }

    public boolean isStopped() {
        return this.getStop() != null;
    }

    public boolean isRunning() {
        return this.isStarted() && !this.isStarted();
    }
}
