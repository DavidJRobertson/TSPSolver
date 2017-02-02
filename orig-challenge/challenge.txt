
              Glasgow TSP Challenge
              ---------------------

Find the shortest tour that covers the 348 locations in file g7k.tsp

The g7k Data Set
----------------
The data set is 348 locations in a 7k radius with the centre in George Square, Glasgow

   http://www.dcs.gla.ac.uk/~pat/ads2/java/tsp/data/g7k.tsp

To find the actual post codes (not relevant, but interesting) look here 

   http://www.dcs.gla.ac.uk/~pat/ads2/java/tsp/data/g7k

The format of the g7k.tsp problem is described below


Code You Can Use
----------------
There is code to read in a tsp, display it, get inter-city cost, cost of a tour,
display a tour, sample code for random search (dumb as you can get), code to 
verify a tour is a tour and cost it, display and verify a tour.

The code is here

    http://www.dcs.gla.ac.uk/~pat/ads2/java/tsp/java/


Downloads
---------
Get the zip file here

    http://www.dcs.gla.ac.uk/~pat/ads2/java/tsp/

What To Do
----------
Generate a tour and email it as an attachment to pat@dcs.gla.ac.uk with subject TSP


Format of a Problem and of a Tour
---------------------------------
 (a) All problems are in the directory data, in files with extension .tsp
 (b) For a problem with n locations there are n lines in the file
 (c) Each line gives the x and y coordinates of a location
 (d) Consider the 1st city as being city 0 and last as city n-1
 (e) Your result (a tour) must be a permutation of the numbers 0 to n-1


Competition Rules
-----------------
 1. The tour must be constructed using a program you wrote
 2. The tour will be costed using the Verify program 
 3. Distance is measured as Euclidean distance.
 3. Shortest tour wins, tie breaking on who gets that tour to me first
 4. You can submit a tour as often as you wish
 5. Current best tour will be posted on the ads2 web site



Patrick Prosser
17/02/2016
