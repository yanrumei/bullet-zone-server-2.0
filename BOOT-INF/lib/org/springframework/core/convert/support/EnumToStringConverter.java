/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionService;
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
/*    */ final class EnumToStringConverter
/*    */   extends AbstractConditionalEnumConverter
/*    */   implements Converter<Enum<?>, String>
/*    */ {
/*    */   public EnumToStringConverter(ConversionService conversionService)
/*    */   {
/* 33 */     super(conversionService);
/*    */   }
/*    */   
/*    */   public String convert(Enum<?> source)
/*    */   {
/* 38 */     return source.name();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\EnumToStringConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */