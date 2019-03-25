package org.apache.log4j;

import java.io.IOException;

public class RollingFileAppender
{
  public RollingFileAppender() {}
  
  public RollingFileAppender(Layout layout, String filename)
    throws IOException
  {}
  
  public RollingFileAppender(Layout layout, String filename, boolean append)
    throws IOException
  {}
  
  public void setMaxBackupIndex(int maxBackups) {}
  
  public void setMaximumFileSize(long maxFileSize) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\RollingFileAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */