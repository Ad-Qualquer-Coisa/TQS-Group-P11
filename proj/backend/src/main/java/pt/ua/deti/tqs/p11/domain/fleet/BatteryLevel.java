package pt.ua.deti.tqs.p11.domain.fleet;

/** Stub: behaviour comes after the failing tests. */
public record BatteryLevel(int percent) {

  public static BatteryLevel of(int percent) {
    throw new UnsupportedOperationException("not implemented");
  }

  public boolean isAtLeast(BatteryLevel minimum) {
    throw new UnsupportedOperationException("not implemented");
  }
}
