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
/*    */ final class EnumToIntegerConverter
/*    */   extends AbstractConditionalEnumConverter
/*    */   implements Converter<Enum<?>, Integer>
/*    */ {
/*    */   public EnumToIntegerConverter(ConversionService conversionService)
/*    */   {
/* 32 */     super(conversionService);
/*    */   }
/*    */   
/*    */   public Integer convert(Enum<?> source)
/*    */   {
/* 37 */     return Integer.valueOf(source.ordinal());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\EnumToIntegerConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */