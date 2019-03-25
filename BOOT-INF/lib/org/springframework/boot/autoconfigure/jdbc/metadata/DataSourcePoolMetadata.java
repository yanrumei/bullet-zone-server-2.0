package org.springframework.boot.autoconfigure.jdbc.metadata;

public abstract interface DataSourcePoolMetadata
{
  public abstract Float getUsage();
  
  public abstract Integer getActive();
  
  public abstract Integer getMax();
  
  public abstract Integer getMin();
  
  public abstract String getValidationQuery();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\DataSourcePoolMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */