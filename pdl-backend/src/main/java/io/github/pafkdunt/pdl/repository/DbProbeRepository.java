package io.github.pafkdunt.pdl.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbProbeRepository {
  private final JdbcTemplate jdbc;

  public DbProbeRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public int one() {
    return jdbc.queryForObject("SELECT 1", Integer.class);
  }
}
