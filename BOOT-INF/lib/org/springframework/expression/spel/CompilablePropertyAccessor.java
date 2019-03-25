package org.springframework.expression.spel;

import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.expression.PropertyAccessor;

public abstract interface CompilablePropertyAccessor
  extends PropertyAccessor, Opcodes
{
  public abstract boolean isCompilable();
  
  public abstract Class<?> getPropertyType();
  
  public abstract void generateCode(String paramString, MethodVisitor paramMethodVisitor, CodeFlow paramCodeFlow);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\CompilablePropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */