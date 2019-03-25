package org.springframework.beans;

public abstract interface PropertyValues
{
  public abstract PropertyValue[] getPropertyValues();
  
  public abstract PropertyValue getPropertyValue(String paramString);
  
  public abstract PropertyValues changesSince(PropertyValues paramPropertyValues);
  
  public abstract boolean contains(String paramString);
  
  public abstract boolean isEmpty();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\PropertyValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */