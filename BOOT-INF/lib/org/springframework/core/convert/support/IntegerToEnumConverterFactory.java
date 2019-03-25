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
/*    */ final class IntegerToEnumConverterFactory
/*    */   implements ConverterFactory<Integer, Enum>
/*    */ {
/*    */   public <T extends Enum> Converter<Integer, T> getConverter(Class<T> targetType)
/*    */   {
/* 34 */     return new IntegerToEnum(ConversionUtils.getEnumType(targetType));
/*    */   }
/*    */   
/*    */   private class IntegerToEnum<T extends Enum> implements Converter<Integer, T>
/*    */   {
/*    */     private final Class<T> enumType;
/*    */     
/*    */     public IntegerToEnum()
/*    */     {
/* 43 */       this.enumType = enumType;
/*    */     }
/*    */     
/*    */     public T convert(Integer source)
/*    */     {
/* 48 */       return ((Enum[])this.enumType.getEnumConstants())[source.intValue()];
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\IntegerToEnumConverterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */