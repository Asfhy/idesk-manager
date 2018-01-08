# iDesk Manager
## About iDeks Manager
iDesk Manager is a Java project intended to ease the task of configuring iDesk desktop manager. iDesk is a light desktop manager for Lightweight Window Managers like OpenBox and IceWM.

The UI is designed with JavaFX, as it seems Swing may be getting deprecated someday.

## Why Java?
I know java is not a Lightweight application, but I'm a software developer that works with Java everyday, so it's the programming language that I have better knowledge of. Also the application is intended to be used from time to time, when a change on the iDesk settings is desired; so it's not so important to be perfectly optimized.

## iDesk support
Main development, at least for now, is made by me at my home computer. I use Manjaro Linux (Arch derived distro) with the latest iDesk version in the repositories: iDesk 0.7.5.

Main workflow goes as follows: The application is run, changes are made and saved and iDesk is restarted to view if the changes are applied.

I tried to make the application enough robust to be able to create a new configuration from scratch if iDesk has been never been runned or configured on a computer. But it may have some bug as I had my desktop configured before starting with the manager.

## Status
The development started only a few days ago. The development is at an early stage where some features are nor supported and others are in progress or buggy.

The final objective of the project is to provide an easy way of changing any of the settings for iDesk, including the options to edit, add or remove desktop icons. In that sense most of the work has been done, but it lacks testing and is probably plenty of bugs and posible flaws.
