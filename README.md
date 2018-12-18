# Liveconting stats
<img src="splash.jpg" width="600">
The favourite counter, pairs, Hall of 3k, Hall of 5k(HoE), Hall of 10k (HoS), first counts, bars, day streak, 1k streak, pincus, odd even ration, 20k days, pee, P5M 1k streak, average counts, 10 to 100k, k's participation, Hours of the day, Hall of Participation and other stats. The stats used to be made with C++ and Javascript earlier but I rewrote them in Java. 

Message me if you notice any bugs.

## Manual

The json files need to be uploaded to /res and /res/lastChatFile.txt needs to be updated manually. The program outputs the stats in wiki tables for reddit, which are in txt files in /output. The code is in /src/ivanludvig/livecounting. 
### Releases
In the /releases you can find a zip and a jar file. If you have JRE installed, download the jar file. Place it in a directory that has the /res folder with the json files and also the /output folder. Alternatively, you can download the whole project and place the file in the root (of the project). If you don't have JRE installed, download the zip archive and unzip it into the described location. Then, run the exe file.
Note: don't run all stats at once.

## Code

I rewrote the program to simplify it, but it turned out not to be so easy. 

Main.java calls the main functions. 

The read() function reads the files from /output and fills the arrays/stats in the HoE, FavouriteCounter, Hours and Pairs class. 

Then, the program calls getJson() and reads the files from /res. After each file it updates the stats and clears the messages ArrayList to ensure the program won't crash due to huge data. It continues reading files until it reaches the last count (stored in /res/lastcount.txt).

Finally, write() is called and all the stats are saved in files.

