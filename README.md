# Liveconting stats

The favourite counter, pairs, Hall of 3k, Hall of 5k(HoE), Hall of 10k (HoS), first counts, bars, day streak, 1k streak, pincus, odd even ration, 20k days, pee, P5M 1k streak, average counts, 10 to 100k, k's participation and Hours of the day stats. The stats used to be made with C++ and Javascript earlier but I rewrote them in Java. 

Message me if you notice any bugs.

## Manual

The json files need to be uploaded to /res and /res/lastChatFile.txt needs to be updated manually. The program outputs the stats in wiki tables for reddit, which are in txt files in /output. The code is in /src/ivanludvig/livecounting. 


## Code

I rewrote the program to simplify it, but it turned out not to be so easy. 

Main.java calls the main functions. 

The read() function reads the files from /output and fills the arrays/stats in the HoE, FavouriteCounter, Hours and Pairs class. 

Then, the program calls getJson() and reads the files from /res. After each file it updates the stats and clears the messages ArrayList to ensure the program won't crash due to huge data. It continues reading files until it reaches the last count (stored in /res/lastcount.txt).

Finally, write() is called and all the stats are saved in files.

