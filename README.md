FYP - Sqlite Visualiser
==
Final year project developed for Lancaster University. A tool to visualize the inner workings of a sqlite database system.  

Aim
==
The main aim is to understand the SQLite file format and systems. While providing a useful tool that can help debug, manipulate and record your own SQLite databases,

Abstract
==
This paper presents a tool designed to visualise the internal workings of a SQLite database file. We start by looking at the SQLite history, systems and file format. Finding the file is a series of fixed sized chunks / pages, and each page is a node in a much larger B-Tree structure. We then look at a model-view-controller style application that can then parse, and present this data in real time, while other systems access the file. To finish off we look at how using TestFX and JUnit have help build a robust application. Following on from that we see that the tool could be improved by polishing up the user interface, with customisation and other minor interactions. The system while complete could be improved with increased performance and adding some much needed features. Lastly we see a future project that could look at the changes made to the database and turn them back into the original SQL queries.     

Building from source
==
Clone the repository:  
> git clone https://github.com/Paulb23/FYP-Sqlite_Visualiser.git  

Cd into the directory:  
> cd FYP-Sqlite_Visualiser  

Build the program:  
> ./gradlew build  

Build can then be found under /build  

Licence
==
MIT
