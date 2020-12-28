# Matching-Application

## Purpose
The Partner Matching Application (or Big-Little matching application) was created to assist in the big-little matching process for my sorority. Each year, new members (or littles) are paired with a older sister to be a mentor (their big). In order to create big-little pairs, all of the bigs and littles will rank their top choices for who they want their big or little to be. Traditionally, a team of board members would have to look through these rankings and make these matches by hand. Hours would be spent trying to create optimal pairs. In order to make this matching process easier, I wanted to create an algorithm that would create optimal pairs for you.

To do this, I modified the Gale-Shapley stable matching algorithm, in order to produce "stable" matches. Matches are considered "unstable" if there are two people who would prefer to be with each other instead of their current partner. Thus, all matches are considered "stable" when there are no "unstable" matches. I had to modify this algorithm to account for various factors: (a) the algorithm should favor the little's preferences, (b) the littles do not rank every single big (and vice versa), so it is possible some girls will not be able to be matched, and (c) if there are more littles than bigs, some bigs will recieve two littles, or a set of "twins". In case c, additional logic had to be implemeted to decide which set of twins led to the most favorable matches. 

Since the algorithm is not always able to match everyone, I decided to create a GUI that allowed the team of board members to start with the computed matches and then edit them and move them around by hand. Additionally, all ranking information would be readily availible to them, and information about the matches they created would be displayed as the matches were changed. A demo of this application in use can be seen on my website. 

## Running the application
The application takes in two CSV files: One that contains the big's name, followed by her top choices for her littles and why she wants that girl as her little. The other file contains the littles name with her top choices for her big and why she wants her. 

Currently, the file names are hard-coded into the program. Lines 33 and 43 in the FileReader class should be replaced with the correct file names from your computer. Using an actual file reader is one of the next items on the to-do list for this project. 
