/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.convert.converter.ConverterFactory;
/*    */ import org.springframework.core.convert.converter.ConverterRegistry;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
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
/*    */ public abstract class ConversionServiceFactory
/*    */ {
/*    */   public static void registerConverters(Set<?> converters, ConverterRegistry registry)
/*    */   {
/* 44 */     if (converters != null) {
/* 45 */       for (Object converter : converters) {
/* 46 */         if ((converter instanceof GenericConverter)) {
/* 47 */           registry.addConverter((GenericConverter)converter);
/*    */         }
/* 49 */         else if ((converter instanceof Converter)) {
/* 50 */           registry.addConverter((Converter)converter);
/*    */         }
/* 52 */         else if ((converter instanceof ConverterFactory)) {
/* 53 */           registry.addConverterFactory((ConverterFactory)converter);
/*    */         }
/*    */         else {
/* 56 */           throw new IllegalArgumentException("Each converter object must implement one of the Converter, ConverterFactory, or GenericConverter interfaces");
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\ConversionServiceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */