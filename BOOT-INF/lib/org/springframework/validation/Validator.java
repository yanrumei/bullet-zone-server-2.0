package org.springframework.validation;

public abstract interface Validator
{
  public abstract boolean supports(Class<?> paramClass);
  
  public abstract void validate(Object paramObject, Errors paramErrors);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\Validator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */