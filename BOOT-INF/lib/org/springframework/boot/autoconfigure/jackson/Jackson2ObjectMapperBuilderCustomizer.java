package org.springframework.boot.autoconfigure.jackson;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public abstract interface Jackson2ObjectMapperBuilderCustomizer
{
  public abstract void customize(Jackson2ObjectMapperBuilder paramJackson2ObjectMapperBuilder);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jackson\Jackson2ObjectMapperBuilderCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */