/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedTypeWithMembers;
/*    */ import com.fasterxml.classmate.members.ResolvedMethod;
/*    */ import java.security.PrivilegedAction;
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
/*    */ public final class GetResolvedMemberMethods
/*    */   implements PrivilegedAction<ResolvedMethod[]>
/*    */ {
/*    */   private final ResolvedTypeWithMembers type;
/*    */   
/*    */   public static GetResolvedMemberMethods action(ResolvedTypeWithMembers type)
/*    */   {
/* 24 */     return new GetResolvedMemberMethods(type);
/*    */   }
/*    */   
/*    */   private GetResolvedMemberMethods(ResolvedTypeWithMembers type) {
/* 28 */     this.type = type;
/*    */   }
/*    */   
/*    */   public ResolvedMethod[] run()
/*    */   {
/* 33 */     return this.type.getMemberMethods();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetResolvedMemberMethods.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */