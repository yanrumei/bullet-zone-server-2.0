/*    */ package org.apache.el.stream;
/*    */ 
/*    */ import javax.el.ELException;
/*    */ import javax.el.LambdaExpression;
/*    */ import org.apache.el.util.MessageFactory;
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
/*    */ public class Optional
/*    */ {
/*    */   private final Object obj;
/* 28 */   static final Optional EMPTY = new Optional(null);
/*    */   
/*    */   Optional(Object obj) {
/* 31 */     this.obj = obj;
/*    */   }
/*    */   
/*    */   public Object get() throws ELException
/*    */   {
/* 36 */     if (this.obj == null) {
/* 37 */       throw new ELException(MessageFactory.get("stream.optional.empty"));
/*    */     }
/* 39 */     return this.obj;
/*    */   }
/*    */   
/*    */ 
/*    */   public void ifPresent(LambdaExpression le)
/*    */   {
/* 45 */     if (this.obj != null) {
/* 46 */       le.invoke(new Object[] { this.obj });
/*    */     }
/*    */   }
/*    */   
/*    */   public Object orElse(Object other)
/*    */   {
/* 52 */     if (this.obj == null) {
/* 53 */       return other;
/*    */     }
/* 55 */     return this.obj;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object orElseGet(Object le)
/*    */   {
/* 61 */     if (this.obj == null)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 67 */       if ((le instanceof LambdaExpression)) {
/* 68 */         return ((LambdaExpression)le).invoke((Object[])null);
/*    */       }
/* 70 */       return le;
/*    */     }
/*    */     
/* 73 */     return this.obj;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\stream\Optional.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */