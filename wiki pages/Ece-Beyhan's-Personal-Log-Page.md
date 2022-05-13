## 21 February - 27 February
* Worked on requirements of project
## 28 February - 6 March
* Contribution to the requirements' presentation
* Made requirements presentation
## 7 March - 13 March
* Worked on the requirements report of the project
## 14 March - 20 March
* Brainstorming user interface ideas about the project
## 21 March - 27 March
* Brainstorming user interface ideas about the project
* Prepared the sketches of Teamder's UI design
* Created a mockup for one part of Teamder's UI design by using SceneBuilder
## 28 March - 3 April
* Contribution to the presentation for Teamder's UI design
* Made the UI presentation
* Contribution to the UI design report
## 4 April - 10 April
* Discussing the implementation of the project
## 11 April - 17 April
* Discussing the implementation of the project
* Understanding the connection between JavaFX, SceneBuilder and IntelliJ IDEA
## 18 April - 24 April
* Prepared the detailed design report
* Learning how to change scenes for GUI
* Added "Find a Match Page" to the repository
## 25 April - 1 May
* Discussed with teammate how to not lose the logged in user when changing scenes
* Discussing filter method to show filtered matches
* Learnt how to show changing data derived from database for filtered matches
* Added tableview to show filtered matches
* Improved Find a Match Controller Class
* Learnt how to add hyperlinks to tableview and how to set what action is going to performed when clicked on them
* Added hyperlinks that connect "find a match page" and "match page"s.
## 2 May - 8 May
* Added tableview objects and hyperlinks for current matches and joined matches in profile controller and in friend controller, set what action is going to performed when user clicked on hyperlinks.
* Discussed how to obtain current matches and joined matches of the user.
* Added filterTodaysMatches method to improve match filter.
* Made improvements on FindMatchController class: 

      Users cannot select the date which is before today's date. 

      When they choose today as date, they will not see matches whose time is passed
* Made improvements on createMatchController class:

      All matches will have a unique name (Added doesMatchNameExist method in Database class to check). If users try to select a name which is chosen before, they will get an error message.

      User cannot select a date which is before today's date. If they do so, they will get an error message.

      When users select today to create a match, they will not be able to select a time which is before current time.

      Before creating a match, users current matches's times and the new match's time that user wants to create will be checked if they coincide. If they do so, user cannot create the new match and gets an error message (Added canUserJoinMatch method in Database class to check).

      Handled exception that can be caused by user input. In time selection, application only accepts time in HH:mm format. If user enters in another format, he will get an error message.
## 9 May - 13 May
* Prepared our presentation for demo
* Prepared the final version of detailed design report
      

      
