import java.io.IOException;

/**
 * Class to represent a corrupt appendable object.
 */
public class CorruptAppendable implements Appendable {

  /**
   * Throws exception instead of appending to a working appendable.
   *
   * @param csq The character sequence to append (no appending performed by this class)
   * @throws IOException On all calls
   */
  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("This is corrupt.");
  }

  /**
   * Throws exception instead of appending to a working appendable.
   *
   * @param csq   The character sequence to append (no appending performed by this class)
   * @param start The index of the first character in the subsequence
   *              (no appending performed by this class)
   * @param end   The index of the character following the last character in the
   *              subsequence (no appending performed by this class)
   * @throws IOException on all calls
   */
  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("This is corrupt.");
  }

  /**
   * Throws exception instead of appending to a working appendable.
   *
   * @param c The character to append (no appending performed by this class)
   * @throws IOException on all calls
   */
  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("This is corrupt.");
  }
}
