package Model.repository;

import Model.domain.IMediaItem;
import java.util.List;
import java.util.UUID;

public interface MediaRepository {
  void save(IMediaItem item);
  boolean delete(UUID id);
  List<IMediaItem> findAll();
}
