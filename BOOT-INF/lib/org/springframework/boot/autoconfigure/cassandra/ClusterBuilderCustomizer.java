package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.driver.core.Cluster.Builder;

public abstract interface ClusterBuilderCustomizer
{
  public abstract void customize(Cluster.Builder paramBuilder);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cassandra\ClusterBuilderCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */