/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.lang.reflect.Member;
/*    */ import java.lang.reflect.Modifier;
/*    */ import org.springframework.asm.Type;
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
/*    */ public class VisibilityPredicate
/*    */   implements Predicate
/*    */ {
/*    */   private boolean protectedOk;
/*    */   private String pkg;
/*    */   private boolean samePackageOk;
/*    */   
/*    */   public VisibilityPredicate(Class source, boolean protectedOk)
/*    */   {
/* 27 */     this.protectedOk = protectedOk;
/*    */     
/*    */ 
/* 30 */     this.samePackageOk = (source.getClassLoader() != null);
/* 31 */     this.pkg = TypeUtils.getPackageName(Type.getType(source));
/*    */   }
/*    */   
/*    */   public boolean evaluate(Object arg) {
/* 35 */     Member member = (Member)arg;
/* 36 */     int mod = member.getModifiers();
/* 37 */     if (Modifier.isPrivate(mod))
/* 38 */       return false;
/* 39 */     if (Modifier.isPublic(mod))
/* 40 */       return true;
/* 41 */     if ((Modifier.isProtected(mod)) && (this.protectedOk))
/*    */     {
/* 43 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 47 */     return (this.samePackageOk) && 
/* 48 */       (this.pkg.equals(TypeUtils.getPackageName(Type.getType(member.getDeclaringClass()))));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\VisibilityPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */