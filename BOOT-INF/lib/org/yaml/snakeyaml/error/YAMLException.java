/*    */ package org.yaml.snakeyaml.error;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class YAMLException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -4738336175050337570L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public YAMLException(String message)
/*    */   {
/* 22 */     super(message);
/*    */   }
/*    */   
/*    */   public YAMLException(Throwable cause) {
/* 26 */     super(cause);
/*    */   }
/*    */   
/*    */   public YAMLException(String message, Throwable cause) {
/* 30 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\error\YAMLException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */