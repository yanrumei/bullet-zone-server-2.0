package org.apache.tomcat;

import java.io.File;
import java.io.IOException;

public abstract interface JarScannerCallback
{
  public abstract void scan(Jar paramJar, String paramString, boolean paramBoolean)
    throws IOException;
  
  public abstract void scan(File paramFile, String paramString, boolean paramBoolean)
    throws IOException;
  
  public abstract void scanWebInfClasses()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\JarScannerCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */