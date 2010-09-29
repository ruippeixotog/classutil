---
title: Change Log for ClassUtil
layout: default
---

Version 0.3:

* Now published to the [Scala Tools Maven repository][], which [SBT][]
  includes by default. Thus, if you're using SBT, it's longer necessary to
  specify a custom repository to find this artifact.

[Scala Tools Maven repository]: http://www.scala-tools.org/repo-releases/
[SBT]: http://code.google.com/p/simple-build-tool/

Version 0.2.2:

* Added `ClassUtil` utility module, with some useful utility methods,
  including: `isPrimitive()` methods that test objects and classes to see
  if they represent primitives; an `isOfType` method that uses a Scala
  `Manifest` to allow runtime tests against generic types; and a
  `loadClass` method that simplifies loading a class from its bytes.
* Added `ScalaObjectToBean`, which takes a Scala object and creates a Java
  bean that wraps it, at runtime.
* Added `ClassNameGenerator`, which generates class names automatically.
* Refactored various internal implementation details, for ease of re-use.
* Updated to released 1.2 version of [ScalaTest][] and converted its
  dependency in [SBT][] to a test-time, not run-time, dependency.

[ScalaTest]: http://scalatest.org/
[SBT]: http://code.google.com/p/simple-build-tool/

Version 0.2.1:

* Updated to build with Scala 2.8.0.final *only*.

Version 0.2:

* Added new `MapToBean` module, which takes a Scala map of type
  `Map[String, Any]` and converts it, on the fly, to a Java Bean. By
  default, it also recursively converts any nested maps of type
  `Map[String, Any]` that it finds. The transformation results in an object
  that can only really be used via reflection; however, that fits fine with
  some APIs that want to receive Java Beans as parameters. For a complete
  description, see
  [the appropriate section on the web site](http://bmc.github.com/classutil/#generating_java_beans_from_scala_maps).
* Updated to version 0.7.1 of [Grizzled Scala][].
* Updated to version 0.2.2 of [Grizzled SLF4J][].
* Removed unnecessary dependency on old version of [Grizzled Scala][] in
  build file.
* 
* Now compiles under Scala 2.8.0.RC5. Now builds against RC3 and RC5 only.

[ASM]: http://asm.ow2.org/
[SBT]: http://code.google.com/p/simple-build-tool
[Grizzled Scala]: http://bmc.github.com/grizzled-scala/
[Grizzled SLF4J]: http://bmc.github.com/grizzled-slf4j/

Version 0.1.2:

* Updated to version 0.7 of [Grizzled Scala][].
* Updated to version 0.2.2 of [Grizzled SLF4J][].
* Bumped to [SBT][] version 0.7.4.
* Tightened access restrictions on some ASM-specific internal classes.
* Now compiles under Scala 2.8.0.RC3 and RC2. Dropped support for RC1.

[SBT]: http://code.google.com/p/simple-build-tool
[Grizzled Scala]: http://bmc.github.com/grizzled-scala/
[Grizzled SLF4J]: http://bmc.github.com/grizzled-slf4j/

Version 0.1.1:

* Now compiles against Scala 2.8.0.RC2, as well as 2.8.0.RC1.


Version 0.1:

* Initial release.