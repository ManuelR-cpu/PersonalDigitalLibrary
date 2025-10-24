public class ConsoleApp {
  public static void main(String[] args) {
    Library myLibrary = new Library();
    LibraryController controller = new LibraryController(myLibrary);
    controller.run();
  }
}
