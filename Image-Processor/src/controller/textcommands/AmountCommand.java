package controller.textcommands;

/**
 * Abstract class that represents a command that takes in
 * an amount along with a name and destination.
 */
public abstract class AmountCommand extends NameDestCommand {
  protected final int amount;

  /**
   * Initialize the name of the image to use for the given command.
   *
   * @param name name to use
   * @param dest destination name to use
   */
  public AmountCommand(int amount, String name, String dest) {
    super(name, dest);
    this.amount = amount;
  }
}
