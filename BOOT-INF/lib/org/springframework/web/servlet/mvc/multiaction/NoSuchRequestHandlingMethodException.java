/*    */ package org.springframework.web.servlet.mvc.multiaction;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.core.style.StylerUtils;
/*    */ import org.springframework.web.util.UrlPathHelper;
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
/*    */ @Deprecated
/*    */ public class NoSuchRequestHandlingMethodException
/*    */   extends ServletException
/*    */ {
/*    */   private String methodName;
/*    */   
/*    */   public NoSuchRequestHandlingMethodException(HttpServletRequest request)
/*    */   {
/* 47 */     this(new UrlPathHelper().getRequestUri(request), request.getMethod(), request.getParameterMap());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public NoSuchRequestHandlingMethodException(String urlPath, String method, Map<String, String[]> parameterMap)
/*    */   {
/* 57 */     super("No matching handler method found for servlet request: path '" + urlPath + "', method '" + method + "', parameters " + 
/* 58 */       StylerUtils.style(parameterMap));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public NoSuchRequestHandlingMethodException(String methodName, Class<?> controllerClass)
/*    */   {
/* 67 */     super("No request handling method with name '" + methodName + "' in class [" + controllerClass
/* 68 */       .getName() + "]");
/* 69 */     this.methodName = methodName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMethodName()
/*    */   {
/* 77 */     return this.methodName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\multiaction\NoSuchRequestHandlingMethodException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */