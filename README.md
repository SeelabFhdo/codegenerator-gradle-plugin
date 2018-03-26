# Generic code generation plugin for Gradle
This Gradle plugin offers a generic api for generating code at compile time with gradle. 
[![](https://api.bintray.com/packages/dfricke/maven/de.fhdo.seelab.CodeGenerator/images/download.svg)](https://bintray.com/dfricke/maven/de.fhdo.seelab.CodeGenerator/_latestVersion)
[![](https://jitpack.io/v/SeelabFhdo/codegenerator-gradle-plugin.svg)](https://jitpack.io/#SeelabFhdo/codegenerator-gradle-plugin) 
[![](https://travis-ci.org/SeelabFhdo/codegenerator-gradle-plugin.svg?branch=master)](https://travis-ci.org/SeelabFhdo/codegenerator-gradle-plugin)

# Usage
```gradle
plugins {
  id 'java'
  id 'de.fhdo.seelab.CodeGenerator' version '0.2.1'
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
    classpath "gradle.plugin.com.github.SeelabFhdo:CodeGeneratorPlugin:0.2.1"
  }
}

apply plugin: "java"
apply plugin: "de.fhdo.seelab.CodeGenerator"

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
  compile 'com.github.SeelabFhdo:codegenerator-gradle-plugin:0.2.1'
}
```

# License

Copyright 2018 Dennis Fricke

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
