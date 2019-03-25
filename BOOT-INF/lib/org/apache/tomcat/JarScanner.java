package org.apache.tomcat;

import javax.servlet.ServletContext;

public abstract interface JarScanner
{
  public abstract void scan(JarScanType paramJarScanType, ServletContext paramServletContext, JarScannerCallback paramJarScannerCallback);
  
  public abstract JarScanFilter getJarScanFilter();
  
  public abstract void setJarScanFilter(JarScanFilter paramJarScanFilter);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\JarScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */