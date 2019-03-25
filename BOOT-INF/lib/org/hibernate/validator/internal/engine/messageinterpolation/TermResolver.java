package org.hibernate.validator.internal.engine.messageinterpolation;

import javax.validation.MessageInterpolator.Context;

public abstract interface TermResolver
{
  public abstract String interpolate(MessageInterpolator.Context paramContext, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\TermResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */