![Minecraft Survival Evolved Logo](http://i.imgur.com/25lm47I.png)
## General
This plugin tries to bring gameplay mechanics from ARK Survival Evolved to Minecraft. Players can join a tribe and together tame NPCs of the Minecraft universe.
## Installation
These instructions focus primarly on Windows. However it's similar (and actually easier) on Linux.
### BuildTools
Download [BuildTools](https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar) and run `java -jar BuildTools.jar --rev <version>` in Git Bash. It will download the sources from Github, create the requiered jar file for a server and copy some requiered dependencies to the local .m2 folder. Currently this project is based on *Spigot 1.9.2*.
### Test Server
Copy the *spigot-x.x.x.jar* file that was created via BuildTools to a folder. The pom.xml uses *C:/Test Server/* as a path for the server so you'll have to change it, if you are using a different location.
Create a .bat file that starts the server (make sure that the name matches your actual spigot.jar file):
```
java -Xms512M -Xmx1024M -XX:MaxPermSize=128M -jar spigot-x.x.x.jar
pause
```
### IntelliJ Settings
Open the project and click on *Edit Configurations*. Add a run configuration for *Maven* using the plus icon and change the *Command line* to `clean package`.
To debug the plugin you can add a run configuration for *Remote* and copy the text from *Command line arguments for running remote JVM* to your startup script so that it looks somewhat like this:
```
java -Xms512M -Xmx1024M -XX:MaxPermSize=128M -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar spigot-1.9.2.jar
pause
```
You can now connect to your server by clicking on *Make Projects* while the debug run configuration is selected:

![Image of run configuration](https://proxy.spigotmc.org/c9297d1f639820db7784fb3ee5b793bdfd84d106?url=http%3A%2F%2Fi.imgur.com%2FNtJickZ.png)

More detailed instructions on how to debug can be found [here](https://www.spigotmc.org/wiki/intellij-debug-your-plugin/).