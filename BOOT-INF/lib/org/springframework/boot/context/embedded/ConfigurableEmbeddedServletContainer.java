package org.springframework.boot.context.embedded;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.boot.web.servlet.ServletContextInitializer;

public abstract interface ConfigurableEmbeddedServletContainer
  extends ErrorPageRegistry
{
  public abstract void setContextPath(String paramString);
  
  public abstract void setDisplayName(String paramString);
  
  public abstract void setPort(int paramInt);
  
  public abstract void setSessionTimeout(int paramInt);
  
  public abstract void setSessionTimeout(int paramInt, TimeUnit paramTimeUnit);
  
  public abstract void setPersistSession(boolean paramBoolean);
  
  public abstract void setSessionStoreDir(File paramFile);
  
  public abstract void setAddress(InetAddress paramInetAddress);
  
  public abstract void setRegisterDefaultServlet(boolean paramBoolean);
  
  public abstract void setErrorPages(Set<? extends ErrorPage> paramSet);
  
  public abstract void setMimeMappings(MimeMappings paramMimeMappings);
  
  public abstract void setDocumentRoot(File paramFile);
  
  public abstract void setInitializers(List<? extends ServletContextInitializer> paramList);
  
  public abstract void addInitializers(ServletContextInitializer... paramVarArgs);
  
  public abstract void setSsl(Ssl paramSsl);
  
  public abstract void setSslStoreProvider(SslStoreProvider paramSslStoreProvider);
  
  public abstract void setJspServlet(JspServlet paramJspServlet);
  
  public abstract void setCompression(Compression paramCompression);
  
  public abstract void setServerHeader(String paramString);
  
  public abstract void setLocaleCharsetMappings(Map<Locale, Charset> paramMap);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\ConfigurableEmbeddedServletContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */