package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract interface NamespaceHandler
{
  public abstract void init();
  
  public abstract BeanDefinition parse(Element paramElement, ParserContext paramParserContext);
  
  public abstract BeanDefinitionHolder decorate(Node paramNode, BeanDefinitionHolder paramBeanDefinitionHolder, ParserContext paramParserContext);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\xml\NamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */