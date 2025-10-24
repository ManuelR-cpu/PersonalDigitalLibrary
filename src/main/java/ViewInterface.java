import java.util.List;

public interface ViewInterface {

  public int showMenuAndGetChoice();

  public void showAllItems(List<IMediaItem> items);

  public void showMessage(String message);

  public String askForTitle(String purpose);

  public IMediaItem askForNewMediaItem();
}
