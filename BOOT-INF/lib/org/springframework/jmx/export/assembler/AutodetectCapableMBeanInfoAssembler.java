package org.springframework.jmx.export.assembler;

public abstract interface AutodetectCapableMBeanInfoAssembler
  extends MBeanInfoAssembler
{
  public abstract boolean includeBean(Class<?> paramClass, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\assembler\AutodetectCapableMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */