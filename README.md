# AtherysGradle

Applies all build script tasks & dependencies in common between plugins.
* Dependencies added
  * SpongeAPI
  * AtherysCore
* Tasks added
  * atherysdoc - A custom javadoc task

## Use
The shadow plugin must be applied before the A'therys one.
```gradle
plugins {
  id 'com.github.johnrengelman.shadow' version '2.0.2'
  id 'com.atherys.gradle' version '1.1.3'
}
```
