/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.convert.converter.ConverterFactory;
/*    */ import org.springframework.util.NumberUtils;
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
/*    */ final class StringToNumberConverterFactory
/*    */   implements ConverterFactory<String, Number>
/*    */ {
/*    */   public <T extends Number> Converter<String, T> getConverter(Class<T> targetType)
/*    */   {
/* 45 */     return new StringToNumber(targetType);
/*    */   }
/*    */   
/*    */   private static final class StringToNumber<T extends Number> implements Converter<String, T>
/*    */   {
/*    */     private final Class<T> targetType;
/*    */     
/*    */     public StringToNumber(Class<T> targetType)
/*    */     {
/* 54 */       this.targetType = targetType;
/*    */     }
/*    */     
/*    */     public T convert(String source)
/*    */     {
/* 59 */       if (source.isEmpty()) {
/* 60 */         return null;
/*    */       }
/* 62 */       return NumberUtils.parseNumber(source, this.targetType);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\StringToNumberConverterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */