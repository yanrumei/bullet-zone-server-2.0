/*    */ package org.yaml.snakeyaml.extensions.compactnotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PackageCompactConstructor
/*    */   extends CompactConstructor
/*    */ {
/*    */   private String packageName;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PackageCompactConstructor(String packageName)
/*    */   {
/* 22 */     this.packageName = packageName;
/*    */   }
/*    */   
/*    */   protected Class<?> getClassForName(String name) throws ClassNotFoundException
/*    */   {
/* 27 */     if (name.indexOf('.') < 0) {
/*    */       try {
/* 29 */         return Class.forName(this.packageName + "." + name);
/*    */       }
/*    */       catch (ClassNotFoundException e) {}
/*    */     }
/*    */     
/*    */ 
/* 35 */     return super.getClassForName(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\extensions\compactnotation\PackageCompactConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */