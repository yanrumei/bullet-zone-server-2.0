/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.util.Properties;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class StringToPropertiesConverter
/*    */   implements Converter<String, Properties>
/*    */ {
/*    */   public Properties convert(String source)
/*    */   {
/*    */     try
/*    */     {
/* 36 */       Properties props = new Properties();
/*    */       
/* 38 */       props.load(new ByteArrayInputStream(source.getBytes("ISO-8859-1")));
/* 39 */       return props;
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 43 */       throw new IllegalArgumentException("Failed to parse [" + source + "] into Properties", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\StringToPropertiesConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */