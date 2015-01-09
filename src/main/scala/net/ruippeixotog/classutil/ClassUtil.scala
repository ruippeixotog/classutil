/*
  ---------------------------------------------------------------------------
  This software is released under a BSD license, adapted from
  http://opensource.org/licenses/bsd-license.php

  Copyright (c) 2010, Brian M. Clapper
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are
  met:

   * Redistributions of source code must retain the above copyright notice,
    this list of conditions and the following disclaimer.

   * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.

   * Neither the names "clapper.org", "ClassUtil", nor the names of its
    contributors may be used to endorse or promote products derived from
    this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
  IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  ---------------------------------------------------------------------------
*/

package net.ruippeixotog.classutil

import scala.reflect.{ClassTag, classTag}
import grizzled.reflect

/** Some general-purpose class-related utility functions.
  */
object ClassUtil {
  private lazy val JavaPrimitives = Set(
    classOf[java.lang.Boolean].   asInstanceOf[Any],
    java.lang.Boolean.TYPE.       asInstanceOf[Any],
    classOf[java.lang.Byte].      asInstanceOf[Any],
    java.lang.Byte.TYPE.          asInstanceOf[Any],
    classOf[java.lang.Character]. asInstanceOf[Any],
    java.lang.Character.TYPE.     asInstanceOf[Any],
    classOf[java.lang.Double].    asInstanceOf[Any],
    java.lang.Double.TYPE.        asInstanceOf[Any],
    classOf[java.lang.Float].     asInstanceOf[Any],
    java.lang.Float.TYPE.         asInstanceOf[Any],
    classOf[java.lang.Integer].   asInstanceOf[Any],
    java.lang.Integer.TYPE.       asInstanceOf[Any],
    classOf[java.lang.Long].      asInstanceOf[Any],
    java.lang.Long.TYPE.          asInstanceOf[Any],
    classOf[java.lang.Short].     asInstanceOf[Any],
    java.lang.Short.TYPE.         asInstanceOf[Any],
    classOf[java.lang.Void].      asInstanceOf[Any],
    java.lang.Void.TYPE.          asInstanceOf[Any]
  )

  // For generating type signatures.
  private[classutil] lazy val PrimitiveSigMap = Map(
    classOf[Boolean].getName -> "Z",
    classOf[Byte].getName    -> "B",
    classOf[Char].getName    -> "C",
    classOf[Short].getName   -> "S",
    classOf[Int].getName     -> "I",
    classOf[Long].getName    -> "J",
    classOf[Float].getName   -> "F",
    classOf[Double].getName  -> "D",
    classOf[Unit].getName    -> "V",
    "void"                   -> "V"
  )

  /** Determine whether an object is a primitive or not.
    *
    * @param obj  the object
    *
    * @return `true` if its class is a primitive, `false` if not.
    */
  def isPrimitive(obj: Any): Boolean =
    isPrimitive(obj.asInstanceOf[AnyRef].getClass)

  /** Determine whether a class represents an underlying primitive or not.
    * For instance, `Int`, `Float` and `Unit` all represent underlying
    * primitives. Note that Java classes are considered primitives if they
    * *are*, in fact, primitives, or if they represent boxed forms of
    * primitives.
    *
    * @param cls  the class
    *
    * @return `true` if the class represents a primitive, `false` if not
    */
  def isPrimitive[T: ClassTag](cls: Class[T]): Boolean = {
    def scalaPrimitive = classTag[T].runtimeClass.toString match {
      case "boolean" => true
      case "byte"    => true
      case "char"    => true
      case "double"  => true
      case "float"   => true
      case "int"     => true
      case "long"    => true
      case "short"   => true
      case "void"    => true
      case _ => false
    }

    def javaPrimitive = JavaPrimitives contains cls

    scalaPrimitive || javaPrimitive
  }

  /** Determine if a value is of, or is assignable to, a specified type.
    * Works with generics. Example of use:
    *
    * {{{
    * val value: Any = ...
    * assert(isOfType[Map[String,Int]](value))
    * }}}
    *
    * @tparam T     the type against which to check the value
    * @param  value the value to check
    *
    * @return whether or not `value` conforms to type `T`
    */
  def isOfType[T: ClassTag](v: Any): Boolean =
    reflect.isOfType[T](v)

  /** Convenience method to load a class from an array of class bytes.
    *
    * @param classLoader  the class loader to use
    * @param className    the name of the class
    * @param classBytes   the class's byte code
    *
    * @return the loaded class
    */
  def loadClass(classLoader: ClassLoader,
                className: String,
                classBytes: Array[Byte]): Class[_] = {
    val cls = Class.forName("java.lang.ClassLoader")
    val method = cls.getDeclaredMethod("defineClass",
                                       classOf[String],
                                       classOf[Array[Byte]],
                                       classOf[Int],
                                       classOf[Int])
    method.setAccessible(true)
    method.invoke(classLoader, className, classBytes,
                  new java.lang.Integer(0),
                  new java.lang.Integer(classBytes.length)).
    asInstanceOf[Class[_]]
  }

  /** Generate a runtime signature for a class (type). For instance:
    *
    * {{{
    * java.lang.String  ->  Ljava/lang/String;
    * int               ->  I
    * }}}
    *
    * @param cls  The class (type)
    *
    * @return its string signature
    */
  def classSignature(cls: Class[_]): String = {
    if (cls.isArray)
      "[" + classSignature(cls.getComponentType)

    else if (cls.isPrimitive) {
      val s = PrimitiveSigMap.get(cls.getName)
      if (s == None)
        throw new Exception("Can't map class \"" + cls.getName + "\" " +
                            "to signature.")
      s.get
    }

      else
        "L" + binaryClassName(cls.getName) + ";"
  }

  /** Generate a runtime signature for a method. See, for instance:
    * [[http://journals.ecs.soton.ac.uk/java/tutorial/native1.1/implementing/method.html]]
    *
    * @param returnType  the method's return type
    * @param paramTypes  the methods parameter types. An empty array
    *                    signifies a method that takes no parameters.
    *
    * @return its string signature
    */
  def methodSignature(returnType: Class[_], paramTypes: Array[Class[_]]) = {
    val paramSig =
      if ((paramTypes == null) || (paramTypes.length == 0))
        ""
      else
        paramTypes.map(pt => classSignature(pt)).mkString("")

    "(" + paramSig + ")" + classSignature(returnType)
  }

  /** Generate a runtime signature for a method. See, for instance:
    * [[http://journals.ecs.soton.ac.uk/java/tutorial/native1.1/implementing/method.html]]
    *
    * @param method  method, from java.lang.reflect
    *
    * @return its string signature
    */
  def methodSignature(method: java.lang.reflect.Method): String =
    methodSignature(method.getReturnType, method.getParameterTypes)

  /** Convert a class name (e.g., "java.lang.String") into its binary
    * form (e.g., "java/lang/String").
    *
    * @param className  the internal class name
    *
    * @return the binary form
    */
  private[classutil] def binaryClassName(className: String): String =
    className.replaceAll("""\.""", "/")
}
