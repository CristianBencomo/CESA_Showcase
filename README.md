# CESA (Car Enhancement System Application)

## Collaborators
Cristian Bencomo, Charles King, Rafael Mesa, Luis Silva, and Ralph Calixte

## General Information
This repository is simply a clone display that does not display the current commits and contributions as that repository is currently private and an unfinished project. 
This code has been used as a proof of concept

## What is CESA
Cesa is an embedded system (prototyped in a raspberry Pi) that connects to vehicles via OBD2 connection, the OBD2 pulls multiple data sets including speed, rpm, engine tempeture, oil level, etc. The device also has a GPS and gyroscope attached for further functionalities. The data collected in the device, is then uploaded to firebase via a hotspot connection, where the data will be stored. The user is able to then open the CESA app and view the data, receive push notifications if the device notices something out of the ordinary and such. These were some of the prototyped concepts but there are other features that were originally planned to be implemented.

## Navigating the Repository
* Android Studio Folder<br />
Here you can find the android studio folder of the app, yyou can see the main source code by navigating to CesaMockV3>app>src>main>java

* RPIConnection<br />
Here you can find multiple pythhon scripts that were tested and used for different stages of development and functionalities, you can find an extra readme file within it