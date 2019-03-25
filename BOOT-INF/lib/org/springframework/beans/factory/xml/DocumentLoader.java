package org.springframework.beans.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public abstract interface DocumentLoader
{
  public abstract Document loadDocument(InputSource paramInputSource, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler, int paramInt, boolean paramBoolean)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\xml\DocumentLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */