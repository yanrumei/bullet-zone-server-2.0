/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalConverter;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ abstract class AbstractConditionalEnumConverter
/*    */   implements ConditionalConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   protected AbstractConditionalEnumConverter(ConversionService conversionService)
/*    */   {
/* 36 */     this.conversionService = conversionService;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/*    */   {
/* 42 */     for (Class<?> interfaceType : ClassUtils.getAllInterfacesForClassAsSet(sourceType.getType())) {
/* 43 */       if (this.conversionService.canConvert(TypeDescriptor.valueOf(interfaceType), targetType)) {
/* 44 */         return false;
/*    */       }
/*    */     }
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\AbstractConditionalEnumConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */