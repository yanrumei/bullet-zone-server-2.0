package org.apache.catalina.ssi;

import java.io.PrintWriter;

public abstract interface SSICommand
{
  public abstract long process(SSIMediator paramSSIMediator, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, PrintWriter paramPrintWriter)
    throws SSIStopProcessingException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSICommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */