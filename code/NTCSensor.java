class NTCSensor extends Sensor {
  private Temperature computeSteinhartFormula(
    Voltage inputVoltage, Voltage supplyVoltage, 
    Resistance r1, Resistance r2, int bValue, 
    Temperature nominalT
  ) {
      var r3 = inputVoltage.getValue() * r1.getValue() 
        / (supplyVoltage.getValue() - inputVoltage.getValue());
      var t = 1 / ((Math.log(r3 / r2.getValue()) / bValue) + 
        1 / (nominalT.getValue() + KELVIN)) - KELVIN;
      return Temperature.of(t);
  }
}
