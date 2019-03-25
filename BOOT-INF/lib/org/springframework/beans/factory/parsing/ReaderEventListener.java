package org.springframework.beans.factory.parsing;

import java.util.EventListener;

public abstract interface ReaderEventListener
  extends EventListener
{
  public abstract void defaultsRegistered(DefaultsDefinition paramDefaultsDefinition);
  
  public abstract void componentRegistered(ComponentDefinition paramComponentDefinition);
  
  public abstract void aliasRegistered(AliasDefinition paramAliasDefinition);
  
  public abstract void importProcessed(ImportDefinition paramImportDefinition);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\ReaderEventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */