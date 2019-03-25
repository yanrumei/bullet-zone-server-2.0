package org.springframework.validation;

import org.springframework.beans.PropertyAccessException;

public abstract interface BindingErrorProcessor
{
  public abstract void processMissingFieldError(String paramString, BindingResult paramBindingResult);
  
  public abstract void processPropertyAccessException(PropertyAccessException paramPropertyAccessException, BindingResult paramBindingResult);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\BindingErrorProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */