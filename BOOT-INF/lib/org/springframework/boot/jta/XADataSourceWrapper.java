package org.springframework.boot.jta;

import javax.sql.DataSource;
import javax.sql.XADataSource;

public abstract interface XADataSourceWrapper
{
  public abstract DataSource wrapDataSource(XADataSource paramXADataSource)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\XADataSourceWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */