package org.hibernate.validator.group;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface GroupSequenceProvider
{
  Class<? extends DefaultGroupSequenceProvider<?>> value();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\group\GroupSequenceProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */