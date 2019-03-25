package org.springframework.web.servlet.mvc.support;

import java.util.Collection;
import java.util.Map;
import org.springframework.ui.Model;

public abstract interface RedirectAttributes
  extends Model
{
  public abstract RedirectAttributes addAttribute(String paramString, Object paramObject);
  
  public abstract RedirectAttributes addAttribute(Object paramObject);
  
  public abstract RedirectAttributes addAllAttributes(Collection<?> paramCollection);
  
  public abstract RedirectAttributes mergeAttributes(Map<String, ?> paramMap);
  
  public abstract RedirectAttributes addFlashAttribute(String paramString, Object paramObject);
  
  public abstract RedirectAttributes addFlashAttribute(Object paramObject);
  
  public abstract Map<String, ?> getFlashAttributes();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\support\RedirectAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */