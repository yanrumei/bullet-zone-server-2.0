package com.fasterxml.jackson.databind.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer.None;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.None;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonDeserialize
{
  Class<? extends JsonDeserializer> using() default JsonDeserializer.None.class;
  
  Class<? extends JsonDeserializer> contentUsing() default JsonDeserializer.None.class;
  
  Class<? extends KeyDeserializer> keyUsing() default KeyDeserializer.None.class;
  
  Class<?> builder() default Void.class;
  
  Class<? extends Converter> converter() default Converter.None.class;
  
  Class<? extends Converter> contentConverter() default Converter.None.class;
  
  Class<?> as() default Void.class;
  
  Class<?> keyAs() default Void.class;
  
  Class<?> contentAs() default Void.class;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\annotation\JsonDeserialize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */