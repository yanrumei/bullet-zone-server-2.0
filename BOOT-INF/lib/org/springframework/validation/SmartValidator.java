package org.springframework.validation;

public abstract interface SmartValidator
  extends Validator
{
  public abstract void validate(Object paramObject, Errors paramErrors, Object... paramVarArgs);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\SmartValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */