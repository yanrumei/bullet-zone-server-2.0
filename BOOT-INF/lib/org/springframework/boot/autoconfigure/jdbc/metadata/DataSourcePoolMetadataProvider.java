package org.springframework.boot.autoconfigure.jdbc.metadata;

import javax.sql.DataSource;

public abstract interface DataSourcePoolMetadataProvider
{
  public abstract DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource paramDataSource);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\DataSourcePoolMetadataProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */