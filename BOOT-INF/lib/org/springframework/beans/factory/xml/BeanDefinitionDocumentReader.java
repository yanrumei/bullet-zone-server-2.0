package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.w3c.dom.Document;

public abstract interface BeanDefinitionDocumentReader
{
  public abstract void registerBeanDefinitions(Document paramDocument, XmlReaderContext paramXmlReaderContext)
    throws BeanDefinitionStoreException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\xml\BeanDefinitionDocumentReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */