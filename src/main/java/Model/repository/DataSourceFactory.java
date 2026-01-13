package Model.repository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

// class to establish connect to database media_items
public class DataSourceFactory {

  public static DataSource createDataSource() {
    String user = System.getenv("DB_USER");
    String password = System.getenv("DB_PASSWORD");

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://localhost:5432/media_items");
    config.setUsername(user);
    config.setPassword(password);

    return new HikariDataSource(config);
  }
}
