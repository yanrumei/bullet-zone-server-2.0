package org.springframework.web.servlet.view.script;

import java.nio.charset.Charset;
import javax.script.ScriptEngine;

public abstract interface ScriptTemplateConfig
{
  public abstract ScriptEngine getEngine();
  
  public abstract String getEngineName();
  
  public abstract Boolean isSharedEngine();
  
  public abstract String[] getScripts();
  
  public abstract String getRenderObject();
  
  public abstract String getRenderFunction();
  
  public abstract String getContentType();
  
  public abstract Charset getCharset();
  
  public abstract String getResourceLoaderPath();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\script\ScriptTemplateConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */