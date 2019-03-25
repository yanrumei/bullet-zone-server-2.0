package org.springframework.cglib.transform.impl;

public abstract interface FieldProvider
{
  public abstract String[] getFieldNames();
  
  public abstract Class[] getFieldTypes();
  
  public abstract void setField(int paramInt, Object paramObject);
  
  public abstract Object getField(int paramInt);
  
  public abstract void setField(String paramString, Object paramObject);
  
  public abstract Object getField(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\transform\impl\FieldProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */