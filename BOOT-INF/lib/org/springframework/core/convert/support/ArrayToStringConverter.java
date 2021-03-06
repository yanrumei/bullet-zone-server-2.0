/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*    */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ final class ArrayToStringConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final CollectionToStringConverter helperConverter;
/*    */   
/*    */   public ArrayToStringConverter(ConversionService conversionService)
/*    */   {
/* 42 */     this.helperConverter = new CollectionToStringConverter(conversionService);
/*    */   }
/*    */   
/*    */ 
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/*    */   {
/* 48 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object[].class, String.class));
/*    */   }
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/*    */   {
/* 53 */     return this.helperConverter.matches(sourceType, targetType);
/*    */   }
/*    */   
/*    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/*    */   {
/* 58 */     return this.helperConverter.convert(Arrays.asList(ObjectUtils.toObjectArray(source)), sourceType, targetType);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\ArrayToStringConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */