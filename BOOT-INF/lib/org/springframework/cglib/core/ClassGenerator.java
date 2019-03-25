package org.springframework.cglib.core;

import org.springframework.asm.ClassVisitor;

public abstract interface ClassGenerator
{
  public abstract void generateClass(ClassVisitor paramClassVisitor)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\ClassGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */