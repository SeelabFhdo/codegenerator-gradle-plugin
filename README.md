# Generic code generation plugin for Gradle
This Gradle plugin offers a generic api for generating code at compile time with gradle. 

[![](https://jitpack.io/v/SeelabFhdo/codegenerator-gradle-plugin.svg)](https://jitpack.io/#SeelabFhdo/codegenerator-gradle-plugin) 
[![](https://travis-ci.org/SeelabFhdo/codegenerator-gradle-plugin.svg?branch=master)](https://travis-ci.org/SeelabFhdo/codegenerator-gradle-plugin)

# Usage
```gradle
plugins {
  id 'java'
  id 'de.seelab.CodeGenerator' version '0.1.3'
}

codeGenerator {
  generatorJar 'libs/my-code-generator.jar'
  generatorJar 'liby/my-second-code-generator.jar'
}
```

## Old style: 
```gradle
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.github.SeelabFhdo:CodeGeneratorPlugin:0.1.3"
  }
}

apply plugin: "java"
apply plugin: "de.seelab.CodeGenerator"

codeGenerator {
  generatorJar 'libs/my-code-generator.jar'
  generatorJar 'liby/my-second-code-generator.jar'
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
  compile 'com.github.SeelabFhdo:codegenerator-gradle-plugin:0.1.3'
}
```
