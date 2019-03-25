/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionException;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.support.DefaultConversionService;
/*    */ import org.springframework.expression.TypeConverter;
/*    */ import org.springframework.expression.spel.SpelEvaluationException;
/*    */ import org.springframework.expression.spel.SpelMessage;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class StandardTypeConverter
/*    */   implements TypeConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public StandardTypeConverter()
/*    */   {
/* 46 */     this.conversionService = DefaultConversionService.getSharedInstance();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public StandardTypeConverter(ConversionService conversionService)
/*    */   {
/* 54 */     Assert.notNull(conversionService, "ConversionService must not be null");
/* 55 */     this.conversionService = conversionService;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType)
/*    */   {
/* 61 */     return this.conversionService.canConvert(sourceType, targetType);
/*    */   }
/*    */   
/*    */   public Object convertValue(Object value, TypeDescriptor sourceType, TypeDescriptor targetType)
/*    */   {
/*    */     try {
/* 67 */       return this.conversionService.convert(value, sourceType, targetType);
/*    */ 
/*    */     }
/*    */     catch (ConversionException ex)
/*    */     {
/* 72 */       throw new SpelEvaluationException(ex, SpelMessage.TYPE_CONVERSION_ERROR, new Object[] {value != null ? value.getClass().getName() : sourceType != null ? sourceType.toString() : "null", targetType.toString() });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\support\StandardTypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */