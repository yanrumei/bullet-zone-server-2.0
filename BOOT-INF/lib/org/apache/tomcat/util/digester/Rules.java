package org.apache.tomcat.util.digester;

import java.util.List;

public abstract interface Rules
{
  public abstract Digester getDigester();
  
  public abstract void setDigester(Digester paramDigester);
  
  @Deprecated
  public abstract String getNamespaceURI();
  
  @Deprecated
  public abstract void setNamespaceURI(String paramString);
  
  public abstract void add(String paramString, Rule paramRule);
  
  public abstract void clear();
  
  public abstract List<Rule> match(String paramString1, String paramString2);
  
  public abstract List<Rule> rules();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\Rules.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */