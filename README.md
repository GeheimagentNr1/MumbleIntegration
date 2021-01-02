# MumbleIntegration

A mod description can be found on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/mumble-integration).

## Used libraries

### java-mumble-link

This Minecraft Forge mod uses the [java-mumble-link](https://github.com/magneticflux-/java-mumble-link) library with the [GNU LGPLv3](https://www.gnu.org/licenses/lgpl-3.0.en.html) licence.  
The copyright holder of the library is [Mitchell Skaggs](https://github.com/magneticflux-).  
Licences affecting this library can be found in the lib_licences folder.

### Using modified version of java-mumble-link

The library is provided as part of the mods jar-file.  
To customize the library version follow the following steps:  
1. Clone this repository  
2. Change the library version by changing the value of the java_mumble_link_version property in the gradle.properites file  
3. Run "gradle shadowJar"  
4. Your custom mod version can be found in the build/libs folder. Copy it to the mods folder of your client/server.  

## Branches

master - default branch - holds only the licences of the libraries and the README file  
develop_X.X.X - develop branch for the Minecraft version X.X.X - holds unstable and possible not compilable or working version.  
master_X.X.X - master branch for the Minecraft version X.X.X - holds stable/released versions of the mod/jar files can be found on CurseForge  
Other branches - branches for implementing new feature - contains unstable and possible not compilable or working version.  
