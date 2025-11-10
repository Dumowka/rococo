package guru.qa.rococo.ex;

public class ArtistNotFoundException extends RuntimeException {
  public ArtistNotFoundException() {
  }

  public ArtistNotFoundException(String message) {
        super(message);
    }
}
