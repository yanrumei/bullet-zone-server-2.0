/*    */ package org.springframework.web.bind;
/*    */ 
/*    */ import javax.servlet.ServletRequest;
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.web.util.WebUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletRequestParameterPropertyValues
/*    */   extends MutablePropertyValues
/*    */ {
/*    */   public static final String DEFAULT_PREFIX_SEPARATOR = "_";
/*    */   
/*    */   public ServletRequestParameterPropertyValues(ServletRequest request)
/*    */   {
/* 52 */     this(request, null, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServletRequestParameterPropertyValues(ServletRequest request, String prefix)
/*    */   {
/* 64 */     this(request, prefix, "_");
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
/*    */   public ServletRequestParameterPropertyValues(ServletRequest request, String prefix, String prefixSeparator)
/*    */   {
/* 77 */     super(WebUtils.getParametersStartingWith(request, prefix != null ? prefix + prefixSeparator : null));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\ServletRequestParameterPropertyValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */