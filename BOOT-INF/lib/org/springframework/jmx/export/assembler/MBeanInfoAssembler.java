package org.springframework.jmx.export.assembler;

import javax.management.JMException;
import javax.management.modelmbean.ModelMBeanInfo;

public abstract interface MBeanInfoAssembler
{
  public abstract ModelMBeanInfo getMBeanInfo(Object paramObject, String paramString)
    throws JMException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\assembler\MBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */