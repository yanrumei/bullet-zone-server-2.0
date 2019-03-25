package org.springframework.scripting;

import java.io.IOException;

public abstract interface ScriptSource
{
  public abstract String getScriptAsString()
    throws IOException;
  
  public abstract boolean isModified();
  
  public abstract String suggestedClassName();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scripting\ScriptSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */