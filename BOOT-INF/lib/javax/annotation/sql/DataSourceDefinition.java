package javax.annotation.sql;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceDefinition
{
  String className();
  
  String name();
  
  String description() default "";
  
  String url() default "";
  
  String user() default "";
  
  String password() default "";
  
  String databaseName() default "";
  
  int portNumber() default -1;
  
  String serverName() default "localhost";
  
  int isolationLevel() default -1;
  
  boolean transactional() default true;
  
  int initialPoolSize() default -1;
  
  int maxPoolSize() default -1;
  
  int minPoolSize() default -1;
  
  int maxIdleTime() default -1;
  
  int maxStatements() default -1;
  
  String[] properties() default {};
  
  int loginTimeout() default 0;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-annotations-api-8.5.27.jar!\javax\annotation\sql\DataSourceDefinition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */