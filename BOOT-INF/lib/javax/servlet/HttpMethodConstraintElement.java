/*    */ package javax.servlet;
/*    */ 
/*    */ import java.util.ResourceBundle;
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
/*    */ public class HttpMethodConstraintElement
/*    */   extends HttpConstraintElement
/*    */ {
/*    */   private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
/* 30 */   private static final ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.LocalStrings");
/*    */   private final String methodName;
/*    */   
/*    */   public HttpMethodConstraintElement(String methodName)
/*    */   {
/* 35 */     if ((methodName == null) || (methodName.length() == 0)) {
/* 36 */       throw new IllegalArgumentException(lStrings.getString("httpMethodConstraintElement.invalidMethod"));
/*    */     }
/*    */     
/* 39 */     this.methodName = methodName;
/*    */   }
/*    */   
/*    */   public HttpMethodConstraintElement(String methodName, HttpConstraintElement constraint)
/*    */   {
/* 44 */     super(constraint.getEmptyRoleSemantic(), constraint
/* 45 */       .getTransportGuarantee(), constraint
/* 46 */       .getRolesAllowed());
/* 47 */     if ((methodName == null) || (methodName.length() == 0)) {
/* 48 */       throw new IllegalArgumentException(lStrings.getString("httpMethodConstraintElement.invalidMethod"));
/*    */     }
/*    */     
/* 51 */     this.methodName = methodName;
/*    */   }
/*    */   
/*    */   public String getMethodName() {
/* 55 */     return this.methodName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\HttpMethodConstraintElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */