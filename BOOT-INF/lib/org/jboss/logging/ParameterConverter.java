package org.jboss.logging;

import java.util.Locale;

public abstract interface ParameterConverter<I>
{
  public abstract Object convert(Locale paramLocale, I paramI);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\ParameterConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */