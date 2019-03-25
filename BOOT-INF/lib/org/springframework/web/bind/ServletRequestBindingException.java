/*    */ package org.springframework.web.bind;
/*    */ 
/*    */ import org.springframework.web.util.NestedServletException;
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
/*    */ public class ServletRequestBindingException
/*    */   extends NestedServletException
/*    */ {
/*    */   public ServletRequestBindingException(String msg)
/*    */   {
/* 40 */     super(msg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServletRequestBindingException(String msg, Throwable cause)
/*    */   {
/* 49 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\ServletRequestBindingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */