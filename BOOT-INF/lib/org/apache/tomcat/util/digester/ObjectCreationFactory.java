package org.apache.tomcat.util.digester;

import org.xml.sax.Attributes;

public abstract interface ObjectCreationFactory
{
  public abstract Object createObject(Attributes paramAttributes)
    throws Exception;
  
  public abstract Digester getDigester();
  
  public abstract void setDigester(Digester paramDigester);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\ObjectCreationFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */