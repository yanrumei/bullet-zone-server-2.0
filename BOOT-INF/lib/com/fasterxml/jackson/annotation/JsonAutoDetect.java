/*    */ package com.fasterxml.jackson.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import java.lang.reflect.Member;
/*    */ import java.lang.reflect.Modifier;
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
/*    */ @Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.TYPE})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @JacksonAnnotation
/*    */ public @interface JsonAutoDetect
/*    */ {
/*    */   Visibility getterVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   Visibility isGetterVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   Visibility setterVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   Visibility creatorVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   Visibility fieldVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   public static enum Visibility
/*    */   {
/* 45 */     ANY, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 50 */     NON_PRIVATE, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 56 */     PROTECTED_AND_PUBLIC, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 61 */     PUBLIC_ONLY, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 67 */     NONE, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 74 */     DEFAULT;
/*    */     
/*    */     private Visibility() {}
/* 77 */     public boolean isVisible(Member m) { switch (JsonAutoDetect.1.$SwitchMap$com$fasterxml$jackson$annotation$JsonAutoDetect$Visibility[ordinal()]) {
/*    */       case 1: 
/* 79 */         return true;
/*    */       case 2: 
/* 81 */         return false;
/*    */       case 3: 
/* 83 */         return !Modifier.isPrivate(m.getModifiers());
/*    */       case 4: 
/* 85 */         if (Modifier.isProtected(m.getModifiers())) {
/* 86 */           return true;
/*    */         }
/*    */       
/*    */       case 5: 
/* 90 */         return Modifier.isPublic(m.getModifiers());
/*    */       }
/* 92 */       return false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-annotations-2.8.0.jar!\com\fasterxml\jackson\annotation\JsonAutoDetect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */