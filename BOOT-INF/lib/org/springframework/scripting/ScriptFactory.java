package org.springframework.scripting;

import java.io.IOException;

public abstract interface ScriptFactory
{
  public abstract String getScriptSourceLocator();
  
  public abstract Class<?>[] getScriptInterfaces();
  
  public abstract boolean requiresConfigInterface();
  
  public abstract Object getScriptedObject(ScriptSource paramScriptSource, Class<?>... paramVarArgs)
    throws IOException, ScriptCompilationException;
  
  public abstract Class<?> getScriptedObjectType(ScriptSource paramScriptSource)
    throws IOException, ScriptCompilationException;
  
  public abstract boolean requiresScriptedObjectRefresh(ScriptSource paramScriptSource);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scripting\ScriptFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */