package org.springframework.ui.context;

public abstract interface HierarchicalThemeSource
  extends ThemeSource
{
  public abstract void setParentThemeSource(ThemeSource paramThemeSource);
  
  public abstract ThemeSource getParentThemeSource();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframewor\\ui\context\HierarchicalThemeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */