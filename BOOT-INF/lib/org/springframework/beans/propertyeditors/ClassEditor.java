/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ 
/*    */ 
/*    */ public class ClassEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public ClassEditor()
/*    */   {
/* 47 */     this(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ClassEditor(ClassLoader classLoader)
/*    */   {
/* 56 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/*    */   }
/*    */   
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 62 */     if (StringUtils.hasText(text)) {
/* 63 */       setValue(ClassUtils.resolveClassName(text.trim(), this.classLoader));
/*    */     }
/*    */     else {
/* 66 */       setValue(null);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getAsText()
/*    */   {
/* 72 */     Class<?> clazz = (Class)getValue();
/* 73 */     if (clazz != null) {
/* 74 */       return ClassUtils.getQualifiedName(clazz);
/*    */     }
/*    */     
/* 77 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\ClassEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */