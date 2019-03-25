/*    */ package javax.el;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodNotFoundException
/*    */   extends ELException
/*    */ {
/*    */   private static final long serialVersionUID = -3631968116081480328L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MethodNotFoundException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MethodNotFoundException(String message)
/*    */   {
/* 28 */     super(message);
/*    */   }
/*    */   
/*    */   public MethodNotFoundException(Throwable cause) {
/* 32 */     super(cause);
/*    */   }
/*    */   
/*    */   public MethodNotFoundException(String message, Throwable cause) {
/* 36 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\MethodNotFoundException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */