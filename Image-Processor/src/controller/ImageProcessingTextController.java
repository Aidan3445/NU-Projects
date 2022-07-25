package controller;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import controller.textcommands.ImageProcessingTextCommand;

/**
 * Interface represents a controller for the user
 * to interact with images and use image processing operations.
 */
public interface ImageProcessingTextController {
  /**
   * Runs the model and beings taking inputs.
   *
   * @throws IllegalStateException if the controller cannot properly read or write
   */
  void run() throws IllegalStateException;

  /**
   * Sets up the commands available to this program.
   * Make sure to add all command objects that should be available to the controller.
   *
   * @return the map that was created
   */
  Map<String, Function<Scanner, ImageProcessingTextCommand>> setupCommands();

  /**
   * The operations available to this program.
   *
   * @throws IOException if transmission to the view fails
   */
  void menu() throws IOException;

  /**
   * Set the source path for folder to load and save images.
   *
   * @throws IOException if transmission to the view fails
   */
  void setSourcePath() throws IOException;
}
