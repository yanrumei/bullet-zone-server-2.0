package org.springframework.format;

import java.util.Locale;

public abstract interface Printer<T>
{
  public abstract String print(T paramT, Locale paramLocale);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\Printer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */