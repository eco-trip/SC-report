class AuthorizationService {
  // ...
  public CompletableFuture<Void> start() {
    return engine
      .submit(authorizationUseCases::bootstrap)
      .thenCompose(u -> this.startNfcTagEmulation());
  }

  private CompletableFuture<Void> startNfcTagEmulation() {
    return CompletableFuture.runAsync(() -> {
      while (true) {
        try {
          authorizationUseCases.waitNearbyDevice().join();
          authorizationUseCases.transmitToken().join();
        } catch (CancellationException | CompletionException ex) {
          logger.log(Level.WARNING, ex.getMessage());
        }
      }
    });
  }
}
