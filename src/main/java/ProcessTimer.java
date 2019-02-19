import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter(AccessLevel.PRIVATE)
/**
 * A ProcessTimer times the execution of a Runnable.
 */
public class ProcessTimer implements TimerLifecycle<ProcessTimer> {

    private Consumer<ProcessTimer> onStart = processTimer -> {
    };
    private Consumer<ProcessTimer> onStop = processTimer -> {
    };

    private Instant start;
    private Instant stop;

    private boolean logStart = true;
    private boolean logStop = true;

    @Override
    public ProcessTimer onStart(Consumer<ProcessTimer> toExecuteOnStart) {
        this.setOnStart(toExecuteOnStart);
        return this;
    }

    @Override
    public ProcessTimer onStop(Consumer<ProcessTimer> toExecuteOnEnd) {
        this.setOnStop(toExecuteOnEnd);
        return this;
    }

    @Override
    public void start() {
        this.getOnStart().accept(this);
        this.setStart(Instant.now());
    }

    @Override
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

    public void time(Runnable toExecute) {

        this.start();
        toExecute.run();
        this.stop();
    }

    public Duration getDuration() {

        if (!this.isStarted()) throw new IllegalStateException("ProcessTimer not started yet.");
        if (this.isRunning()) return Duration.between(this.getStart(), Instant.now());
        return Duration.between(this.getStart(), this.getStop());
    }

    public boolean isStarted() {
        return this.getStart() != null;
    }

    public boolean isStopped() {
        return this.getStop() != null;
    }

    public boolean isRunning() {
        return this.isStarted() && !this.isStopped();
    }
}
