# Theocratic-picture-viewer
This is a java swing project that detects usb and navigates images in it.

The customer requested that:

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
its about java
1. Watch for removable media, like a USB drive, plugged into a USB port. When detected, show a list of all the folders on the root of the drive.

2. Allow a folder to be selected.

3. When a folder is selected import all the pictures within the folder. At the same time, copy the files to local disc so that the program can continue to use the pictures when the media is removed.

4. Show the file names in alphabetical order on the main UI and show a small thumbnail preview of the picture above file name. The UI should occupy about 50% of the screen.

5. When a picture is clicked, show the picture on the secondary screen, full screen. Resize the image so that it is stretched/scaled to fill the entire secondary screen, if the picture is wider than it is tall.

6. Only stretch and scale the picture on the secondary screen if it is wider than its height. If the picture is taller than it is wide then scale the picture top to bottom and scale width proportionately, but don't stretch the width and center the picture.

7. The list of files should have a visible 'break' feature between each file (just show text "Break"). The objective is to use the down arrow key on the computer keyboard to show an image, then show the desktop (when in break mode), then arrow down to next image, then desktop, then 3rd image, then desktop, and so forth.

8. The operator will use the up/down keys to go through the list of pictures on the UI, so start with a 'break' at the top of the list, which means that the desktop on the secondary screen would be showing. The initial 'break' should visibly have focus with a colored bar around the text (note the green box around the first "Break" button in the drawing attached). When the operator uses the arrow down key to go to the next picture, show the picture until the down arrow is pressed again which then shows the desktop on the secondary screen.

9. When the program is closed delete all files from the temp directory. The program should always start fresh upon next launch.

10. Allow program to be minimized on main UI but keep the image maximized on the secondary screen if an image is showing.

11. Make the secondary form background black, so that it is black to the left and right of a picture that is taller than it is wide.

12. Make package and deploy installer to install the program to Program Files (x86)/Picture Viewer and place an icon on the desktop. A simple icon will suffice.

13. The program should support PNG, GIF, JPG, JPEG, PDF, and TIF image file types.

14. Allow user to reorganize the pictures, by either dragging and dropping, or by selecting and moving up/down.

15. Allow arrow up and down keys to navigate the list of pictures, and when the picture has focus post it on the secondary screen, scaled as defined above, but also allow the user to point and click on either the pictures or the break points between the pictures.

16. At the bottom of the UI, have a "Remove Selected" button. This should allow the user to remove the selected picture on the UI. First confirm "Do you really want to remove the selected image?" Have yes/no options.

17. Also, include a button "Clear All". When clicked clear out all pictures loaded in the UI, and also clear from local disc. First confirm "Do you really want to clear all images?" Have yes/no options.

18. An "Exit" button closes the application.

19. If removable media is added again, after the program as already loaded in the pictures, clear all pictures from the UI and local disc, and start from the beginning with prompting which folder on the removable media to import the pictures from.

The source code is required upon project completion.
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


So I did it and upload to here.
