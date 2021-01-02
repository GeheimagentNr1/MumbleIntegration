# MumbleIntegration

A mod description can be found on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/mumble-integration).

## Used libraries

### java-mumble-link

This Minecraft Forge mod uses the [java-mumble-link](https://github.com/magneticflux-/java-mumble-link) library with the [GNU LGPLv3](https://www.gnu.org/licenses/lgpl-3.0.en.html) licence.  
The copyright holder of the library is [Mitchell Skaggs](https://github.com/magneticflux-).  
Licences affecting this library can be found in the lib_licences folder.

## Changes of the library version

The library is provided with the mods jar-file.  
To change the library version change the value of the java_mumble_link_version property in the gradle.properties file.  
Then you have to run a gradle sync and when the sync is done you have to run "gradle shadowJar".  
You will find your custom version in the folder build/libs.  
There are no warranties, that the mod works with a custom library version.

### Branches
master - default branch - holds only the licences of the libraries and the README file  
develop_X.X.X - develop branch for the Minecraft version X.X.X - holds unstable and possible not compilable or working version.  
master_X.X.X - master branch for the Minecraft version X.X.X - holds stable/released versions of the mod/jar files can be found on CurseForge  
Other branches - branches for implementing new feature - contains unstable and possible not compilable or working version.  
