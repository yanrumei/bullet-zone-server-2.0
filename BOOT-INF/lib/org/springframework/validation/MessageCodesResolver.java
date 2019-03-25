package org.springframework.validation;

public abstract interface MessageCodesResolver
{
  public abstract String[] resolveMessageCodes(String paramString1, String paramString2);
  
  public abstract String[] resolveMessageCodes(String paramString1, String paramString2, String paramString3, Class<?> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\MessageCodesResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */