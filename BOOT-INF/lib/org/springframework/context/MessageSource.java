package org.springframework.context;

import java.util.Locale;

public abstract interface MessageSource
{
  public abstract String getMessage(String paramString1, Object[] paramArrayOfObject, String paramString2, Locale paramLocale);
  
  public abstract String getMessage(String paramString, Object[] paramArrayOfObject, Locale paramLocale)
    throws NoSuchMessageException;
  
  public abstract String getMessage(MessageSourceResolvable paramMessageSourceResolvable, Locale paramLocale)
    throws NoSuchMessageException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\MessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */