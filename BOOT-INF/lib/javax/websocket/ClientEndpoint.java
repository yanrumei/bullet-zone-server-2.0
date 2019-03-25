package javax.websocket;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface ClientEndpoint
{
  String[] subprotocols() default {};
  
  Class<? extends Decoder>[] decoders() default {};
  
  Class<? extends Encoder>[] encoders() default {};
  
  Class<? extends ClientEndpointConfig.Configurator> configurator() default ClientEndpointConfig.Configurator.class;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\ClientEndpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */