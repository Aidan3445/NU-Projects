package model;

import java.util.List;

/**
 * Interface represents a read-only version of the model.
 */
public interface ImageProcessingViewModel {
  /**
   * Get an image with a given name from an image processing model.
   *
   * @param name name of the image as it is kept within the model
   * @return the image object
   * @throws IllegalArgumentException if the name does not exist in the model
   */
  Image getImage(String name) throws IllegalArgumentException;

  /**
   * Get a list of names for all images held in the model.
   *
   * @return the list of names
   */
  List<String> getAll();
}
