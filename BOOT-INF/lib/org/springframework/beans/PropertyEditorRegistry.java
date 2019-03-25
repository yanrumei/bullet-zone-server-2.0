package org.springframework.beans;

import java.beans.PropertyEditor;

public abstract interface PropertyEditorRegistry
{
  public abstract void registerCustomEditor(Class<?> paramClass, PropertyEditor paramPropertyEditor);
  
  public abstract void registerCustomEditor(Class<?> paramClass, String paramString, PropertyEditor paramPropertyEditor);
  
  public abstract PropertyEditor findCustomEditor(Class<?> paramClass, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\PropertyEditorRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */