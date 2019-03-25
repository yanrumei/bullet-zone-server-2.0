/*     */ package org.apache.tomcat.util.bcel.classfile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaClass
/*     */ {
/*     */   private final int access_flags;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String class_name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String superclass_name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String[] interface_names;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Annotations runtimeVisibleAnnotations;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   JavaClass(String class_name, String superclass_name, int access_flags, ConstantPool constant_pool, String[] interface_names, Annotations runtimeVisibleAnnotations)
/*     */   {
/*  49 */     this.access_flags = access_flags;
/*  50 */     this.runtimeVisibleAnnotations = runtimeVisibleAnnotations;
/*  51 */     this.class_name = class_name;
/*  52 */     this.superclass_name = superclass_name;
/*  53 */     this.interface_names = interface_names;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int getAccessFlags()
/*     */   {
/*  60 */     return this.access_flags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationEntry[] getAnnotationEntries()
/*     */   {
/*  70 */     if (this.runtimeVisibleAnnotations != null) {
/*  71 */       return this.runtimeVisibleAnnotations.getAnnotationEntries();
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  80 */     return this.class_name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getInterfaceNames()
/*     */   {
/*  88 */     return this.interface_names;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSuperclassName()
/*     */   {
/* 100 */     return this.superclass_name;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\JavaClass.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */