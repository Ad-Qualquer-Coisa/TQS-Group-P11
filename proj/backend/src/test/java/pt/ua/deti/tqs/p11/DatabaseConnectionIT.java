package pt.ua.deti.tqs.p11;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class DatabaseConnectionIT {

  @Autowired private DataSource dataSource;

  @Test
  void dataSource_connectsToPostgresql() throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      assertThat(connection.isValid(2)).isTrue();
      assertThat(connection.getMetaData().getDatabaseProductName()).isEqualTo("PostgreSQL");
    }
  }
}
