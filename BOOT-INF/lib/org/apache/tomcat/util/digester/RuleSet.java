package org.apache.tomcat.util.digester;

public abstract interface RuleSet
{
  @Deprecated
  public abstract String getNamespaceURI();
  
  public abstract void addRuleInstances(Digester paramDigester);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\RuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */