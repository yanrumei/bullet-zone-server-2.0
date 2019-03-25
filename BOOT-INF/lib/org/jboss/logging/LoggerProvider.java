package org.jboss.logging;

import java.util.Map;

public abstract interface LoggerProvider
{
  public abstract Logger getLogger(String paramString);
  
  public abstract void clearMdc();
  
  public abstract Object putMdc(String paramString, Object paramObject);
  
  public abstract Object getMdc(String paramString);
  
  public abstract void removeMdc(String paramString);
  
  public abstract Map<String, Object> getMdcMap();
  
  public abstract void clearNdc();
  
  public abstract String getNdc();
  
  public abstract int getNdcDepth();
  
  public abstract String popNdc();
  
  public abstract String peekNdc();
  
  public abstract void pushNdc(String paramString);
  
  public abstract void setNdcMaxDepth(int paramInt);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\LoggerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */