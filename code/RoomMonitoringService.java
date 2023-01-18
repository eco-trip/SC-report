class RoomMonitoringService {
  // ...
  public CompletableFuture<Void> start() {
    return engine.schedule(() -> {
      var futures = List.of(
        environmentUseCases
          .detectRoomBrightness(),
        environmentUseCases
          .detectRoomTemperatureAndHumidity(),
        environmentUseCases
          .detectHotWaterTemperature(),
        environmentUseCases
          .detectColdWaterTemperature(),
        computeConsumptionAverage(
          consumptionUseCases::detectCurrent), 
        computeConsumptionAverage(
          consumptionUseCases::detectColdFlowRate), 
        computeConsumptionAverage(
          consumptionUseCases::detectHotFlowRate)
      );
      Futures.thenAll(futures, this::sendData, DEFAULT_DETECT_INTERVAL);
    }, detectionInterval);
  }
}
