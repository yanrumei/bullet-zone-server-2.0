package org.springframework.beans;

import org.springframework.core.convert.ConversionService;

public abstract interface ConfigurablePropertyAccessor
  extends PropertyAccessor, PropertyEditorRegistry, TypeConverter
{
  public abstract void setConversionService(ConversionService paramConversionService);
  
  public abstract ConversionService getConversionService();
  
  public abstract void setExtractOldValueForEditor(boolean paramBoolean);
  
  public abstract boolean isExtractOldValueForEditor();
  
  public abstract void setAutoGrowNestedPaths(boolean paramBoolean);
  
  public abstract boolean isAutoGrowNestedPaths();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\ConfigurablePropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */