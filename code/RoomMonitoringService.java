class RoomMonitoringService {
  // ...
  
  public CompletableFuture<Void> start() {
    return engine.schedule(() -> {
      var currentConsumption = 
        computeConsumptionAverage(
          consumptionUseCases::detectCurrent
        );
      var hotFlowRateConsumption =
        computeConsumptionAverage(
          consumptionUseCases::detectHotFlowRate
        );
      var coldFlowRateConsumption = 
        computeConsumptionAver(
          consumptionUseCases::detectColdFlowRate
        );
      var futures = List.of(
        environmentUseCases
          .detectRoomBrightness(),
        environmentUseCases
          .detectRoomTemperatureAndHumidity(),
        environmentUseCases
          .detectHotWaterTemperature(),
        environmentUseCases
          .detectColdWaterTemperature(),
        currentConsumption, 
        coldFlowRateConsumption, 
        hotFlowRateConsumption
      );
      Futures.thenAll(futures, this::sendData, DEFAULT_DETECT_INTERVAL);
    }, detectionInterval);
  }
}
