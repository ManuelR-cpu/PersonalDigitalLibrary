package Model.domain.api;

import Model.domain.IMediaItem;
import Model.domain.Movie;
import Model.domain.TVShow;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApiClient {
  private HttpClient client;
  private ObjectMapper mapper;
  private String baseUrl;

  public ApiClient(HttpClient client, ObjectMapper mapper, String baseUrl) {
    this.client = client;
    this.mapper = mapper;
    this.baseUrl = baseUrl;
  }

  public List<IMediaItem> getAllItems() {
    List<IMediaItem> items = new ArrayList<>();
    items.addAll(getMovies());
    items.addAll(getTVShows());

    return items;
  }

  public List<Movie> getMoviesByTitle(String title) {
    if (title == null || title.isBlank()) {
      return List.of();
    }

    try {
      String encoded = URLEncoder.encode(title, StandardCharsets.UTF_8);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/movies-search?title=" + encoded))
          .header("Accept", "application/json")
          .GET()
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new RuntimeException(
            "Search failed: " + response.statusCode() + " - " + response.body()
        );
      }

      return mapper.readValue(
          response.body(),
          new TypeReference<>() {}
      );
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to search movies. ", e);
    }
  }

  public List<TVShow> getTVShowsByTitle(String title) {
    if (title == null || title.isBlank()) {
      return List.of();
    }

    try {
      String encoded = URLEncoder.encode(title, StandardCharsets.UTF_8);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/tv-shows-search?title=" + encoded))
          .header("Accept", "application/json")
          .GET()
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new RuntimeException(
            "Search failed: " + response.statusCode() + " - " + response.body()
        );
      }

      return mapper.readValue(
          response.body(),
          new TypeReference<>() {}
      );
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to search TV shows. ", e);
    }
  }

  public List<Movie> getMovies() {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/movies"))
          .GET()
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new RuntimeException(
            "Request failed: " + response.statusCode() + " - " + response.body()
        );
      }

      return mapper.readValue(
          response.body(),
          new TypeReference<>() {}
      );
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to fetch movies. ", e);
    }
  }

  public List<TVShow> getTVShows() {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/tv-shows"))
          .GET()
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new RuntimeException(
            "Request failed: " + response.statusCode() + " - " + response.body()
        );
      }

      return mapper.readValue(
          response.body(),
          new TypeReference<>() {}
      );
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to fetch TV shows. ", e);
    }
  }

  public Movie createMovie(Movie movie) {
    try {
      String json = mapper.writeValueAsString(movie);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/movies"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 201) {
        throw new RuntimeException(
            "Create failed: " + response.statusCode() + " - " + response.body()
        );
      }

      return movie;
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to create movie. ", e);
    }
  }

  public TVShow createTVShow(TVShow show) {
    try {
      String json = mapper.writeValueAsString(show);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/tv-shows"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 201) {
        throw new RuntimeException(
            "Create failed: " + response.statusCode() + " - " + response.body()
        );
      }

      return show;
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to create TV show. ", e);
    }
  }

  public Movie updateMovie(Movie movie) {
    try {
      String json = mapper.writeValueAsString(movie);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/movies/" + movie.getId()))
          .header("Content-Type", "application/json")
          .PUT(HttpRequest.BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new RuntimeException(
            "Update failed: " + response.statusCode() + " - " + response.body()
        );
      }

      return movie;
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to update movie. ", e);
    }
  }

  public TVShow updateTVShow(TVShow show) {
    try {
      String json = mapper.writeValueAsString(show);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/tv-shows/" + show.getId()))
          .header("Content-Type", "application/json")
          .PUT(HttpRequest.BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new RuntimeException(
            "Update failed: " + response.statusCode() + " - " + response.body()
        );
      }

      return show;
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to update TV show. ", e);
    }
  }

  public void deleteMovie(UUID id) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/movies/" + id))
          .DELETE()
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 204) {
        throw new RuntimeException(
            "Delete failed: " + response.statusCode() + " - " + response.body()
        );
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to delete movie. ", e);
    }
  }

  public void deleteTVShow(UUID id) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/tv-shows/" + id))
          .DELETE()
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 204) {
        throw new RuntimeException(
            "Delete failed: " + response.statusCode() + " - " + response.body()
        );
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to delete TV show. ", e);
    }
  }
}
