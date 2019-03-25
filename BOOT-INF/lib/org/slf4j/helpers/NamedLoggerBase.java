/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import java.io.Serializable;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ abstract class NamedLoggerBase
/*    */   implements Logger, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 7535258609338176893L;
/*    */   protected String name;
/*    */   
/*    */   public String getName()
/*    */   {
/* 48 */     return this.name;
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
/*    */ 
/*    */ 
/*    */   protected Object readResolve()
/*    */     throws ObjectStreamException
/*    */   {
/* 68 */     return LoggerFactory.getLogger(getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\slf4j-api-1.7.25.jar!\org\slf4j\helpers\NamedLoggerBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */