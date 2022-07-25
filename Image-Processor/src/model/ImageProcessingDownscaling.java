package model;

/**
 * Interface represents an image processor with an additional downscale operation.
 */
public interface ImageProcessingDownscaling {

  /**
   * Downscales an image by a given percent in each direction.
   *
   * @param pctX percent to shrink image horizontally
   * @param pctY percent to shrink image vertically
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if pctX or pctY are less than 0 or greater than 100
   */
  void downscale(int pctX, int pctY, String name, String dest);
}
