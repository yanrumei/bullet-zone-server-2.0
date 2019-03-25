package org.springframework.cglib.proxy;

import java.util.List;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.Signature;

abstract interface CallbackGenerator
{
  public abstract void generate(ClassEmitter paramClassEmitter, Context paramContext, List paramList)
    throws Exception;
  
  public abstract void generateStatic(CodeEmitter paramCodeEmitter, Context paramContext, List paramList)
    throws Exception;
  
  public static abstract interface Context
  {
    public abstract ClassLoader getClassLoader();
    
    public abstract CodeEmitter beginMethod(ClassEmitter paramClassEmitter, MethodInfo paramMethodInfo);
    
    public abstract int getOriginalModifiers(MethodInfo paramMethodInfo);
    
    public abstract int getIndex(MethodInfo paramMethodInfo);
    
    public abstract void emitCallback(CodeEmitter paramCodeEmitter, int paramInt);
    
    public abstract Signature getImplSignature(MethodInfo paramMethodInfo);
    
    public abstract void emitLoadArgsAndInvoke(CodeEmitter paramCodeEmitter, MethodInfo paramMethodInfo);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\proxy\CallbackGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */