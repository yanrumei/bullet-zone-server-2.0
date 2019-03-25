package org.springframework.cglib.core;

import org.springframework.asm.Type;

public abstract interface FieldTypeCustomizer
  extends KeyFactoryCustomizer
{
  public abstract void customize(CodeEmitter paramCodeEmitter, int paramInt, Type paramType);
  
  public abstract Type getOutType(int paramInt, Type paramType);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\FieldTypeCustomizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */