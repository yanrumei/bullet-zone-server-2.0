/*    */ package org.springframework.web.bind;
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
/*    */ public class MissingServletRequestParameterException
/*    */   extends ServletRequestBindingException
/*    */ {
/*    */   private final String parameterName;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final String parameterType;
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
/*    */   public MissingServletRequestParameterException(String parameterName, String parameterType)
/*    */   {
/* 39 */     super("");
/* 40 */     this.parameterName = parameterName;
/* 41 */     this.parameterType = parameterType;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 47 */     return "Required " + this.parameterType + " parameter '" + this.parameterName + "' is not present";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final String getParameterName()
/*    */   {
/* 54 */     return this.parameterName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final String getParameterType()
/*    */   {
/* 61 */     return this.parameterType;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\MissingServletRequestParameterException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */