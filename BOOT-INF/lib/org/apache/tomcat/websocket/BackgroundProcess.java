package org.apache.tomcat.websocket;

public abstract interface BackgroundProcess
{
  public abstract void backgroundProcess();
  
  public abstract void setProcessPeriod(int paramInt);
  
  public abstract int getProcessPeriod();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\BackgroundProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */