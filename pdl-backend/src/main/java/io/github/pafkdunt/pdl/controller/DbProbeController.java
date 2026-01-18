package io.github.pafkdunt.pdl.controller;

import io.github.pafkdunt.pdl.repository.DbProbeRepository;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbProbeController {
  private final DbProbeRepository repo;

  public DbProbeController(DbProbeRepository repo) {
    this.repo = repo;
  }

  @GetMapping("/db-probe")
  public Map<String, Object> probe() {
    return Map.of("db", "ok", "value", repo.one());
  }
}
