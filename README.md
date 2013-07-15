EZTask
============

What is it?

EZTask is a Task Manager desktop application, created using Java. 

------------

What does it do?

1. Calendar and Priority lists:

EZTask allows the user to add event for a particular date and time( Calendar) or an event with indefinite time or no deadline(Priority).
Calendar list displays all the events entered by the user in an orderly function. All events are displayed in an increasing order based on the date/time slot. The deadline and times tasks are displayed in this list.
Priority list displays the events that do not have a deadline and are to be completed whenever the user prefers to. All floating tasks are displayed in this list.
We can prioritise the Priority List events on the basis of the importance they hold. There are three levels of priorities: low, medium and high represented by 1, 2 and 3. The tasks are sorted based on their priority in descending order with the highest priority tasks at the top.

2. Flexible commands:

All the commands have a flexible feature whereby the command is performed with any of the substrings of the command keyword.
Special flexi features:

ADD:
Date can have any of the following formats:
 Yyyymmdd
 Now/today
 Dd <substring of month in string> <year> (in any order)
Time can have:
 Now
 Hhmm
 Hh (am/pm)
Time follows a 24-hour clock. But is displayed in the 12-hour format.

3. Feature Window/File menu:

EZTask has a features window which gives details on the formats for each of the commands. This acts like a tutorial for a first time user before he gets accustomed to the format of the commands.
The user can also use the help menu/button for further aid. which will lead to our user/developer guide.
The file menu has a number of sub-menus for all the commands which will automatically display the syntax of the command.

4. Log:

Stores the commands entered by the user in past transactions for the five most recent ones and displays it in a text area which has a scroll pane enabled to view the past transactions.
