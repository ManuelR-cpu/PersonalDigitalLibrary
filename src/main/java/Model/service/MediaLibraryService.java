package Model.service;

import Model.domain.IMediaItem;
import Model.domain.MediaType;
import Model.repository.MediaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MediaLibraryService implements LibraryService {
  private final MediaRepository mediaRepository;

  public MediaLibraryService(MediaRepository mediaRepository) {
    this.mediaRepository = mediaRepository;
  }


  @Override
  public void addItem(IMediaItem item) {
    mediaRepository.save(item);
  }

  @Override
  public List<IMediaItem> getAllItems() {
    return mediaRepository.findAll();
  }

  @Override
  public List<IMediaItem> getItemsByType(MediaType type) {
    List<IMediaItem> filteredItems = new ArrayList<>();
    // make one db query here instead of calling findAll() in loop
    List<IMediaItem> allItems = mediaRepository.findAll();

    for (IMediaItem item : allItems) {
      if (item.getMediaType() == type) {
        filteredItems.add(item);
      }
    }

    return filteredItems;
  }

  @Override
  public List<IMediaItem> searchForItems(String title) {
    if (title == null || title.isBlank()) {
      return List.of();
    }

    List<IMediaItem> foundItems = new ArrayList<>();
    // make one db query here instead of calling findAll() in loop
    List<IMediaItem> allItems = mediaRepository.findAll();

    for (IMediaItem item : allItems) {
      if (item.getTitle().toLowerCase().contains(title.toLowerCase())) {
        foundItems.add(item);
      }
    }

    return foundItems;
  }

  @Override
  public boolean deleteItem(UUID uuid) {
    return mediaRepository.delete(uuid);
  }
}
