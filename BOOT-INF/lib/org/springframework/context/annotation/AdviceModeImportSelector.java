/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.core.GenericTypeResolver;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotationMetadata;
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
/*    */ public abstract class AdviceModeImportSelector<A extends Annotation>
/*    */   implements ImportSelector
/*    */ {
/*    */   public static final String DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME = "mode";
/*    */   
/*    */   protected String getAdviceModeAttributeName()
/*    */   {
/* 45 */     return "mode";
/*    */   }
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
/*    */   public final String[] selectImports(AnnotationMetadata importingClassMetadata)
/*    */   {
/* 62 */     Class<?> annoType = GenericTypeResolver.resolveTypeArgument(getClass(), AdviceModeImportSelector.class);
/* 63 */     AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(importingClassMetadata, annoType);
/* 64 */     if (attributes == null) {
/* 65 */       throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", new Object[] {annoType
/*    */       
/* 67 */         .getSimpleName(), importingClassMetadata.getClassName() }));
/*    */     }
/*    */     
/* 70 */     AdviceMode adviceMode = (AdviceMode)attributes.getEnum(getAdviceModeAttributeName());
/* 71 */     String[] imports = selectImports(adviceMode);
/* 72 */     if (imports == null) {
/* 73 */       throw new IllegalArgumentException(String.format("Unknown AdviceMode: '%s'", new Object[] { adviceMode }));
/*    */     }
/* 75 */     return imports;
/*    */   }
/*    */   
/*    */   protected abstract String[] selectImports(AdviceMode paramAdviceMode);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\AdviceModeImportSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */