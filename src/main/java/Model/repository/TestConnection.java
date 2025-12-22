package Model.repository;

import java.sql.Connection;

public class TestConnection {
  public static void main(String[] args) throws Exception {

    var ds = DataSourceFactory.createDataSource();
    try (Connection conn = ds.getConnection()) {
      System.out.println("Connected to database: " + conn.getCatalog());
    }
  }
}
