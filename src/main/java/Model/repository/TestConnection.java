package Model.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestConnection {
  public static void main(String[] args) throws Exception {
    var ds = DataSourceFactory.createDataSource();

    try (Connection conn = ds.getConnection()) {
      System.out.println("=== Connection Debug ===");
      System.out.println("Meta URL : " + conn.getMetaData().getURL());
      System.out.println("Meta User: " + conn.getMetaData().getUserName());
      System.out.println("Catalog  : " + conn.getCatalog()); // often null in Postgres; that's okay
      System.out.println("========================");
      System.out.println();

      // What DB are we ACTUALLY connected to?
      try (PreparedStatement ps = conn.prepareStatement(
          "SELECT current_database(), current_schema()"
      );
           ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          System.out.println("current_database(): " + rs.getString(1));
          System.out.println("current_schema()  : " + rs.getString(2));
        }
      }

      System.out.println();
      System.out.println("Tables visible to the app:");
      // Lists tables similarly to \dt (public schema)
      try (PreparedStatement ps = conn.prepareStatement(
          "SELECT tablename FROM pg_tables WHERE schemaname = 'public' ORDER BY tablename"
      );
           ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          System.out.println(" - " + rs.getString(1));
        }
      }
    }
  }
}
