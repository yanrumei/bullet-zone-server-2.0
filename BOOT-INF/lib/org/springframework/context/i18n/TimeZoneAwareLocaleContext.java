package org.springframework.context.i18n;

import java.util.TimeZone;

public abstract interface TimeZoneAwareLocaleContext
  extends LocaleContext
{
  public abstract TimeZone getTimeZone();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\i18n\TimeZoneAwareLocaleContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */