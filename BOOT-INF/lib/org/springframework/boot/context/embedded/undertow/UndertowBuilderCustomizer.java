package org.springframework.boot.context.embedded.undertow;

import io.undertow.Undertow.Builder;

public abstract interface UndertowBuilderCustomizer
{
  public abstract void customize(Undertow.Builder paramBuilder);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedde\\undertow\UndertowBuilderCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */