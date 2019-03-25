/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.convert.converter.ConverterFactory;
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
/*    */ final class StringToEnumConverterFactory
/*    */   implements ConverterFactory<String, Enum>
/*    */ {
/*    */   public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType)
/*    */   {
/* 34 */     return new StringToEnum(ConversionUtils.getEnumType(targetType));
/*    */   }
/*    */   
/*    */   private class StringToEnum<T extends Enum> implements Converter<String, T>
/*    */   {
/*    */     private final Class<T> enumType;
/*    */     
/*    */     public StringToEnum()
/*    */     {
/* 43 */       this.enumType = enumType;
/*    */     }
/*    */     
/*    */     public T convert(String source)
/*    */     {
/* 48 */       if (source.isEmpty())
/*    */       {
/* 50 */         return null;
/*    */       }
/* 52 */       return Enum.valueOf(this.enumType, source.trim());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\StringToEnumConverterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */