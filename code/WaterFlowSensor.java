public class WaterFlowHallSensor extends Sensor {
  //...
  private WaterFlowHallSensor(...) {
    digitalInput.addListener(e -> {
      if (e.state() == HIGH) pulses.incrementAndGet();
    });
  }

  protected CompletableFuture<List<Measure>> measure() {
    return CompletableFuture.supplyAsync(() -> {
      pulses.set(0);
      double actualTime = Execution
        .getComputationalTimeInMillis(
          this::receivePulsesWithinOneSecond
        );
      return computeFlowRate(actualTime);
    }).thenApply(List::of);
  }

  private FlowRate computeFlowRate(double actualTime) {
    var pulses = (pulses.get() * actualTime) / 1000;
    return FlowRate.of(pulses / frequency);
  }
}