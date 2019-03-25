package org.springframework.boot.autoconfigure.elasticsearch.jest;

import io.searchbox.client.config.HttpClientConfig.Builder;

public abstract interface HttpClientConfigBuilderCustomizer
{
  public abstract void customize(HttpClientConfig.Builder paramBuilder);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\elasticsearch\jest\HttpClientConfigBuilderCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */