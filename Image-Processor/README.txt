DESIGN CHANGES FROM ASSIGNMENT 5 AND JUSTIFICATIONS
- In the ImageProcessorImpl class, we fixed gaussianBlur() to require a positive int greater than 0.

DESIGN CHANGES FROM ASSIGNMENT 4 AND JUSTIFICATIONS
- After completing the self-eval for assignment 4, we realized that the load and save functionality
belonged in the controller since both functions rely on user input, which the controller is in
charge of. Thus, we moved these two features to the controller and removed save from the model
interface. However, the load function in the controller must still call the load function in the
model since it must store the image given to it in the private HashMap.
- We also realized that using a HashMap for the commands instead of a switch statement will be
better, especially in the long-term, so we won't have to keep adding new cases to an already long
list of cases. Instead, we'll implement a new class, add the new command to the ImageProcessingModel
Interface and implement in the appropriate classes, which for right now is just ImageProcessorPPM,
and put the new command in the HashMap, thereby reducing the amount of code we'll need to change in
order to add a new command. As such, we removed each command's original implementation from
ImageProcessingTextController.
    - Created a new method setUpCommands() that initializes the map of commands
- Added menu() and setSourcePath() to the ImageProcessingController Interface after realizing that
these are both things that we want available to all controllers and not just a text controller.
- Since we added a kernel method in ImageProcessorPPM to support applying an effect to an area,
we changed process() to pass a kernel size of 1 to kernel().
- Renamed ImageProcessorPPM class to ImageProcessorImpl after realizing that this class would hold
support for all types of image files
    - Added support for transparency in all operations so transparency is preserved after performing
      any image processing operation
- In the ImageImpl class, we chose to change a method and catch a new exceptions since we decided
to support transparency
    - Changed the order of the error message for getPixel() to row and then column instead of the
      other way around because it sounds more like conventional English.
    - Checked to make sure that the length of the RGBA array was 4 instead of 3 (if we were not
      supporting transparency, it would be an RGB array).
- In the ImageUtil class, we made the methods strictly for reading and saving a PPM file private
static since we now have methods called readImage() and saveImage that can load and save generic
image file types respectively
    - Added a private static method called getExtension() to get the extension of any file. This
      allowed us to create generic methods for reading and saving an image file instead of writing
      a new method for each new image file type we have to handle.
    - moved the main method to ImageProcessorProgram class since it makes more sense to have that
      outside as its own class.

CITATIONS FOR IMAGES USED
Penguin image is free software and used under the terms of the GNU Lesser General Public License
without warranty. It is the Tux Crystal 1st revision image.
Specific link: https://commons.wikimedia.org/wiki/File:Crystal_128_penguin.png
The sky picture is the one referenced in the assignment on Canvas, we don't have access to that
picture source (but we assume we have fair use to it since it was provided to us).
The rainbow looking picture is used as personal use for this project. It will not be used for any
commercial purpose.
Specific link: https://wallpaperaccess.com/large
ALl other images in /res were created by us to use for this project.

NOTES
1. If running tests does not initially work on the grader's machine, please see line 11 of
    ImageProcessorPPMMTest and change the resPath per your file management system.
2. In the save test (line 100 in AbstractImageProcessorTest), lines 117-122 delete the file that
    is created in the test in case the grader wants to run the tests multiple times.

THE RESOURCES DIRECTORY
This directory contains the source image (penguin) as each file format, as well as the result of
each operation performed on the penguin.png to show that transparency is still supported. There is
also another directory named 'script' nested within this one, which is to hold the images created
after running the exampleScript.txt file provided. This subdirectory distinguishes which images
are intended for grading purposes and which ones were created to show the functionality of the
script file in text format.

OUR DESIGN
I. THE MODEL
The model now supports bmp, jpg, and png files in addition to ppm files. Our design from last
assignment allowed us to support these new file types without making too many design changes.
There is one caveat to our design: while a user can read a png image that has transparent
components, if the image is saved as another file type, it will lose its transparency. However, if
the png file is read and saved as a png, the transparency will carry over.
1. interfaces
    a. ImageProcessingModel represents the image processing operations that can be performed
        on an image.
    b. ImageProcessingViewModel represents a read-only version of the model.
    c. Image represents the operations available to an image.
    d. Function3 represents a function that accepts 3 arguments and produces a results. This is a
        functional interface whose functional method is apply(Object).
2. classes
    a. ImageProcessorImpl represents a processor for image files.
       Changed description from last time.
    b. ImageUtil is a utility class to read a PPM image from a file and save a PPM image to a file.
    c. ImageImpl represents an image.

II. THE VIEW
The view currently consists of a text view, which shows the width, height, and maximum color value
of a given image when called upon.
1. interfaces
    a. ImageProcessingView represents a view for an image processor displayed to the user.
2. classes
    a. ImageProcessingTextView displays user interaction with an image via text user interface.

III. THE CONTROLLER
We made the image processing features an interface because we assume we will be building on this
assignment. As such, all images would have access to these operations.
1. interfaces
    a. ImageProcessingController represents a controller for the user to interact with images
        and use image processing operations.
    b. ImageProcessingFeatures holds the image processing features available.
2. classes
    a. ImageProcessingTextController represents the controller to allow user to perform
        image processing effects via text interface.
3. commands package
    a. ImageProcessingCommand interface
        i. This interface provides one method named execute() that executes this command on the
           provided model and view classes. This interface is implemented in each of the abstract
           classes and each command's class.
    b. The abstract classes
        i. NameDestCommand provides a constructor that initializes the name of the image to use and
           the name to save the image as. It also has a method named modelViewException() that
           checks if the model and/or view objects provided are null.
        ii. AmountCommand extends NameDestCommand provides a constructor that calls the super
            constructor and initializes the amount to use for certain commands like brighten and
            blur.
    c. Each Command class provides a constructor to initialize its data and implements execute() to
       call on its respective method and pass a message to the view if the operation was
       successfully performed.