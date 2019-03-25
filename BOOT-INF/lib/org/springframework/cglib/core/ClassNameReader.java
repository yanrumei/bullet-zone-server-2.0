/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.ClassReader;
/*    */ import org.springframework.asm.ClassVisitor;
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
/*    */ public class ClassNameReader
/*    */ {
/* 29 */   private static final EarlyExitException EARLY_EXIT = new EarlyExitException(null);
/*    */   
/*    */ 
/*    */   public static String getClassName(ClassReader r)
/*    */   {
/* 34 */     return getClassInfo(r)[0];
/*    */   }
/*    */   
/*    */   public static String[] getClassInfo(ClassReader r)
/*    */   {
/* 39 */     final List array = new ArrayList();
/*    */     try {
/* 41 */       r.accept(new ClassVisitor(393216, null)
/*    */       {
/*    */ 
/*    */ 
/*    */         public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
/*    */         {
/*    */ 
/* 48 */           array.add(name.replace('/', '.'));
/* 49 */           if (superName != null) {
/* 50 */             array.add(superName.replace('/', '.'));
/*    */           }
/* 52 */           for (int i = 0; i < interfaces.length; i++) {
/* 53 */             array.add(interfaces[i].replace('/', '.'));
/*    */           }
/*    */           
/* 56 */           throw ClassNameReader.EARLY_EXIT; } }, 6);
/*    */     }
/*    */     catch (EarlyExitException localEarlyExitException) {}
/*    */     
/*    */ 
/* 61 */     return (String[])array.toArray(new String[0]);
/*    */   }
/*    */   
/*    */   private static class EarlyExitException
/*    */     extends RuntimeException
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\ClassNameReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */