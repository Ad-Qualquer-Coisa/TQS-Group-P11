package pt.ua.deti.tqs.p11.domain.fleet;

/** Battery charge of a vehicle, as a percentage from 0 to 100. */
public record BatteryLevel(int percent) {

  private static final int MIN_PERCENT = 0;
  private static final int MAX_PERCENT = 100;

  public BatteryLevel {
    if (percent < MIN_PERCENT || percent > MAX_PERCENT) {
      throw new IllegalArgumentException(
          "Battery percent must be between %d and %d: %d"
              .formatted(MIN_PERCENT, MAX_PERCENT, percent));
    }
  }

  public static BatteryLevel of(int percent) {
    return new BatteryLevel(percent);
  }

  /** Business rule BR1: a vehicle is bookable only at or above the minimum level. */
  public boolean isAtLeast(BatteryLevel minimum) {
    return percent >= minimum.percent;
  }
}
