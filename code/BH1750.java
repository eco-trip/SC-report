class BH1750 {
  private final I2C channel;
  //...
  protected CompletableFuture<List<Measure>> measure() {
    return CompletableFuture.supplyAsync(() -> {
        byte[] p = new byte[2];
        channel.read(p, 0, 2);
        int msb = p[0] & 0xff;
        int lsb = p[1] & 0xff;
        return Brightness.of((msb << 8) + lsb);
    }).thenApply(List::of);
  }
}
