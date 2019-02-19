import java.util.function.Consumer;

public interface TimerLifecycle<E> {

    void start();

    void stop();

    E onStart(Consumer<E> toExecuteOnStart);

    E onStop(Consumer<E> toExecuteOnEnd);
}
