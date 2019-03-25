package org.springframework.validation;

import java.util.List;

public abstract interface Errors
{
  public static final String NESTED_PATH_SEPARATOR = ".";
  
  public abstract String getObjectName();
  
  public abstract void setNestedPath(String paramString);
  
  public abstract String getNestedPath();
  
  public abstract void pushNestedPath(String paramString);
  
  public abstract void popNestedPath()
    throws IllegalStateException;
  
  public abstract void reject(String paramString);
  
  public abstract void reject(String paramString1, String paramString2);
  
  public abstract void reject(String paramString1, Object[] paramArrayOfObject, String paramString2);
  
  public abstract void rejectValue(String paramString1, String paramString2);
  
  public abstract void rejectValue(String paramString1, String paramString2, String paramString3);
  
  public abstract void rejectValue(String paramString1, String paramString2, Object[] paramArrayOfObject, String paramString3);
  
  public abstract void addAllErrors(Errors paramErrors);
  
  public abstract boolean hasErrors();
  
  public abstract int getErrorCount();
  
  public abstract List<ObjectError> getAllErrors();
  
  public abstract boolean hasGlobalErrors();
  
  public abstract int getGlobalErrorCount();
  
  public abstract List<ObjectError> getGlobalErrors();
  
  public abstract ObjectError getGlobalError();
  
  public abstract boolean hasFieldErrors();
  
  public abstract int getFieldErrorCount();
  
  public abstract List<FieldError> getFieldErrors();
  
  public abstract FieldError getFieldError();
  
  public abstract boolean hasFieldErrors(String paramString);
  
  public abstract int getFieldErrorCount(String paramString);
  
  public abstract List<FieldError> getFieldErrors(String paramString);
  
  public abstract FieldError getFieldError(String paramString);
  
  public abstract Object getFieldValue(String paramString);
  
  public abstract Class<?> getFieldType(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\Errors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */