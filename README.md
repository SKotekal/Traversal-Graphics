# Traversal-Graphics

Accepts input image and calculates average pixel color. Fills in rectangle with average pixel color and then calculates variance between average pixel color and actual image. If the error is above a certain threshold, the current rectangle is cut into four quadrants, and the process repeats until the error threshold is reached. Shape can be changed from a rectangle to a circle. Areas where there is a single color without much variance is not broken down as much since the error threshold is not as large. This idea can be used for compressing images.

The algorithm uses a queue to store all of the current rectangles that need to be broken. The rectangles are timed to broken simultaneously so that the entire image is broken down at once instead of one quadrant at a time. This is more aesthetically pleasing than watching one quadrant render after another. 

Below are some samples.

![Screenshot](http://i.imgur.com/R3Ag9yk.jpg)

![Screenshot](http://i.imgur.com/HQxnG3h.jpg)

Copycat of Michael Fogleman's Quads found at http://www.michaelfogleman.com/
