# Traversal-Graphics

Accepts input image and calculates average pixel color. Fills in rectangle with average pixel color and then calculates variance between average pixel color and actual image. If the error is above a certain threshold, the current rectangle is cut into four quadrants, and the process repeats until the error threshold is reached. Shape can be changed from a rectangle to a circle. Areas where there is a single color without much variance is not broken down as much since the error threshold is not as large. This idea can be used for compressing images.

Below are some samples.

![Screenshot](http://i.imgur.com/R3Ag9yk.jpg)

Copycat of Michael Fogleman's Quads found at http://www.michaelfogleman.com/
