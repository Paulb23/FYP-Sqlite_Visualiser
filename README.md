FYP - Sqlite Visualiser
==
This final year project, implements and designs an application to visualise the file system found within SQlite. It also logs all the changes that have occurred to to the file. It presents the visualisation of the file in a B-Tree format, that allows users to navigate and see how the SQLite file is put together. Alongside this when ever a change is made from any program the visualisation is updated in real time to reflect this. The changes themselves are then recorded inside a log, that the user can browse. Lastly, it allows the user to navigate through the time-line of changes that have occurred since the program started monitoring the database.     

Abstract
==
This paper presents a tool designed to visualise the internal workings of a SQLite database file. Starting with the history of SQLite, its systems and file format. Finding the file is a series of fixed sized chunks / pages, and each page is a node in a much larger B-Tree structure. Secondly, constructing a model-view-controller style application that can then parse, and present this data in real time, while other systems access the file. Thirdly, how using TestFX and JUnit have helped build a robust application. Lastly, how the tool could be improved by polishing up the user interface, with customisation and other minor interactions. The overall system could be improved with increased performance and adding some much needed features. On top of this, a future project could look at the changes made to the database and turn them back into the original SQL queries.        

Building from source
==
Clone the repository:  
> git clone https://github.com/Paulb23/FYP-Sqlite_Visualiser.git  

Cd into the directory:  
> cd FYP-Sqlite_Visualiser  

Build the program:  
> ./gradlew build  

Build can then be found under /build/libs  
Run with:  
> java -jar ./sqlite-visualiser-1.0.jar  

Report pdf or tex documents can be found under /report  

Licence
==
MIT
