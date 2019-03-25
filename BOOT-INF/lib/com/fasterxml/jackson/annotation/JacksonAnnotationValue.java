package com.fasterxml.jackson.annotation;

import java.lang.annotation.Annotation;

public abstract interface JacksonAnnotationValue<A extends Annotation>
{
  public abstract Class<A> valueFor();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-annotations-2.8.0.jar!\com\fasterxml\jackson\annotation\JacksonAnnotationValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */