# Generic code generation plugin for Gradle
This Gradle plugin offers a generic api for generating code at compile time with gradle. 

[![](https://jitpack.io/v/SeelabFhdo/codegenerator-gradle-plugin.svg)](https://jitpack.io/#SeelabFhdo/codegenerator-gradle-plugin) 
[![](https://travis-ci.org/SeelabFhdo/codegenerator-gradle-plugin.svg?branch=master)](https://travis-ci.org/SeelabFhdo/codegenerator-gradle-plugin.svg?branch=master)

# Usage
```gradle
plugins {
  id 'de.seelab.CodeGenerator'
}

codeGenerator {
  generatorJars ['libs/my-code-generator.jar']
}
```

## Code generator
```gradle
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  compile 'com.github.SeelabFhdo:codegenerator-gradle-plugin:0.1'
}
```
