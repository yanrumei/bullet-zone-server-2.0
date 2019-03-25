package org.springframework.core.style;

public abstract interface ToStringStyler
{
  public abstract void styleStart(StringBuilder paramStringBuilder, Object paramObject);
  
  public abstract void styleEnd(StringBuilder paramStringBuilder, Object paramObject);
  
  public abstract void styleField(StringBuilder paramStringBuilder, String paramString, Object paramObject);
  
  public abstract void styleValue(StringBuilder paramStringBuilder, Object paramObject);
  
  public abstract void styleFieldSeparator(StringBuilder paramStringBuilder);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\style\ToStringStyler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */