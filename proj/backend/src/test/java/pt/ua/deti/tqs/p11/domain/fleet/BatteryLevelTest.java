package pt.ua.deti.tqs.p11.domain.fleet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BatteryLevelTest {

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 99, 100})
  void of_percentWithinRange_keepsThePercent(int percent) {
    assertThat(BatteryLevel.of(percent).percent()).isEqualTo(percent);
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 101})
  void of_percentOutsideRange_isRejected(int percent) {
    assertThatThrownBy(() -> BatteryLevel.of(percent)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void isAtLeast_oneBelowTheMinimum_isFalse() {
    assertThat(BatteryLevel.of(19).isAtLeast(BatteryLevel.of(20))).isFalse();
  }

  @Test
  void isAtLeast_exactlyTheMinimum_isTrue() {
    assertThat(BatteryLevel.of(20).isAtLeast(BatteryLevel.of(20))).isTrue();
  }

  @Test
  void isAtLeast_aboveTheMinimum_isTrue() {
    assertThat(BatteryLevel.of(21).isAtLeast(BatteryLevel.of(20))).isTrue();
  }
}
