package org.springframework.web.context.request;

public abstract interface RequestAttributes
{
  public static final int SCOPE_REQUEST = 0;
  public static final int SCOPE_SESSION = 1;
  public static final int SCOPE_GLOBAL_SESSION = 2;
  public static final String REFERENCE_REQUEST = "request";
  public static final String REFERENCE_SESSION = "session";
  
  public abstract Object getAttribute(String paramString, int paramInt);
  
  public abstract void setAttribute(String paramString, Object paramObject, int paramInt);
  
  public abstract void removeAttribute(String paramString, int paramInt);
  
  public abstract String[] getAttributeNames(int paramInt);
  
  public abstract void registerDestructionCallback(String paramString, Runnable paramRunnable, int paramInt);
  
  public abstract Object resolveReference(String paramString);
  
  public abstract String getSessionId();
  
  public abstract Object getSessionMutex();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\RequestAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */