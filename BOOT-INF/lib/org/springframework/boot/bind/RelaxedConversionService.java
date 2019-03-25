/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterFactory;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.core.convert.support.GenericConversionService;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RelaxedConversionService
/*     */   implements ConversionService
/*     */ {
/*     */   private final ConversionService conversionService;
/*     */   private final GenericConversionService additionalConverters;
/*     */   
/*     */   RelaxedConversionService(ConversionService conversionService)
/*     */   {
/*  50 */     this.conversionService = conversionService;
/*  51 */     this.additionalConverters = new GenericConversionService();
/*  52 */     DefaultConversionService.addCollectionConverters(this.additionalConverters);
/*  53 */     this.additionalConverters
/*  54 */       .addConverterFactory(new StringToEnumIgnoringCaseConverterFactory(null));
/*  55 */     this.additionalConverters.addConverter(new StringToCharArrayConverter());
/*     */   }
/*     */   
/*     */   public boolean canConvert(Class<?> sourceType, Class<?> targetType)
/*     */   {
/*  60 */     return ((this.conversionService != null) && 
/*  61 */       (this.conversionService.canConvert(sourceType, targetType))) || 
/*  62 */       (this.additionalConverters.canConvert(sourceType, targetType));
/*     */   }
/*     */   
/*     */   public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType)
/*     */   {
/*  67 */     return ((this.conversionService != null) && 
/*  68 */       (this.conversionService.canConvert(sourceType, targetType))) || 
/*  69 */       (this.additionalConverters.canConvert(sourceType, targetType));
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T convert(Object source, Class<T> targetType)
/*     */   {
/*  75 */     Assert.notNull(targetType, "The targetType to convert to cannot be null");
/*  76 */     return (T)convert(source, TypeDescriptor.forObject(source), 
/*  77 */       TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */   
/*     */ 
/*     */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/*     */   {
/*  83 */     if (this.conversionService != null) {
/*     */       try {
/*  85 */         return this.conversionService.convert(source, sourceType, targetType);
/*     */       }
/*     */       catch (ConversionFailedException localConversionFailedException) {}
/*     */     }
/*     */     
/*     */ 
/*  91 */     return this.additionalConverters.convert(source, sourceType, targetType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class StringToEnumIgnoringCaseConverterFactory
/*     */     implements ConverterFactory<String, Enum>
/*     */   {
/*     */     public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType)
/*     */     {
/* 104 */       Class<?> enumType = targetType;
/* 105 */       while ((enumType != null) && (!enumType.isEnum())) {
/* 106 */         enumType = enumType.getSuperclass();
/*     */       }
/* 108 */       Assert.notNull(enumType, "The target type " + targetType.getName() + " does not refer to an enum");
/*     */       
/* 110 */       return new StringToEnum(enumType);
/*     */     }
/*     */     
/*     */     private class StringToEnum<T extends Enum> implements Converter<String, T>
/*     */     {
/*     */       private final Class<T> enumType;
/*     */       
/*     */       StringToEnum() {
/* 118 */         this.enumType = enumType;
/*     */       }
/*     */       
/*     */       public T convert(String source)
/*     */       {
/* 123 */         if (source.isEmpty())
/*     */         {
/* 125 */           return null;
/*     */         }
/* 127 */         source = source.trim();
/* 128 */         for (T candidate : EnumSet.allOf(this.enumType))
/*     */         {
/* 130 */           RelaxedNames names = new RelaxedNames(candidate.name().replace('_', '-').toLowerCase());
/* 131 */           for (String name : names) {
/* 132 */             if (name.equals(source)) {
/* 133 */               return candidate;
/*     */             }
/*     */           }
/* 136 */           if (candidate.name().equalsIgnoreCase(source)) {
/* 137 */             return candidate;
/*     */           }
/*     */         }
/*     */         
/* 141 */         throw new IllegalArgumentException("No enum constant " + this.enumType.getCanonicalName() + "." + source);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\RelaxedConversionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */