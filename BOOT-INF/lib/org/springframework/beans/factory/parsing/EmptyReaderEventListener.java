package org.springframework.beans.factory.parsing;

public class EmptyReaderEventListener
  implements ReaderEventListener
{
  public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {}
  
  public void componentRegistered(ComponentDefinition componentDefinition) {}
  
  public void aliasRegistered(AliasDefinition aliasDefinition) {}
  
  public void importProcessed(ImportDefinition importDefinition) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\EmptyReaderEventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */