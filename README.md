# Aevum
## Time code execution

[![Build Status](https://travis-ci.org/artus/aevum.svg?branch=master)](https://travis-ci.org/artus/aevum)

## Example

```java
ProcessTimer myTimer = new ProcessTimer();
myTimer
    .onStart(timer -> System.out.println("Started counting to 10"))
    .onStop(timer -> System.out.println("Stopped counting to 10"))
    .time(() -> {
        int count = 0;
        while (count < 10) System.out.println(++count);
    });

System.out.println(String.format("Counting to 10 took %d milliseconds.", myTimer.getDuration().toMillis()));
```

