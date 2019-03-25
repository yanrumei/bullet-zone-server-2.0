package org.springframework.format;

import java.text.ParseException;
import java.util.Locale;

public abstract interface Parser<T>
{
  public abstract T parse(String paramString, Locale paramLocale)
    throws ParseException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */