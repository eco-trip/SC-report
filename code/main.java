public static void main(String[] args) {
  // RoomMonitoring service
  Engine engine = EngineFactory.createScheduledEngine(1);
  var roomMonitoringService = 
    RoomMonitoringService.of(
      engine,
      consumptionUseCases,
      environmentUseCases,
      detectionFactory,
      awsAdapter,
      serializer
    );

  // Authorization service
  Engine engine2 = EngineFactory.createScheduledEngine(1);
  var authUseCases = AuthorizationUseCases.of(
    awsAdapter, nfcAdapter
  );
  var authService = AuthorizationService.of(
    engine2, authUseCases
  );

  // AWS IoT adapter
  awsAdapter.addObserver(authorizationService);
  awsAdapter.connect()
    .thenCompose(u -> CompletableFuture.allOf(
      authorizationService.start(), 
      roomMonitoringService.start())
    )
    .exceptionally(t -> {
      t.printStackTrace();
      awsAdapter.disconnect();
      pi4j.shutdown();
    })
    .join();
}