/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.servlet.ServletRequest;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.web.bind.ServletRequestDataBinder;
/*    */ import org.springframework.web.servlet.HandlerMapping;
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
/*    */ public class ExtendedServletRequestDataBinder
/*    */   extends ServletRequestDataBinder
/*    */ {
/*    */   public ExtendedServletRequestDataBinder(Object target)
/*    */   {
/* 43 */     super(target);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ExtendedServletRequestDataBinder(Object target, String objectName)
/*    */   {
/* 54 */     super(target, objectName);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request)
/*    */   {
/* 64 */     String attr = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
/* 65 */     Map<String, String> uriVars = (Map)request.getAttribute(attr);
/* 66 */     if (uriVars != null) {
/* 67 */       for (Map.Entry<String, String> entry : uriVars.entrySet()) {
/* 68 */         if (mpvs.contains((String)entry.getKey())) {
/* 69 */           if (logger.isWarnEnabled()) {
/* 70 */             logger.warn("Skipping URI variable '" + (String)entry.getKey() + "' since the request contains a bind value with the same name.");
/*    */           }
/*    */           
/*    */         }
/*    */         else {
/* 75 */           mpvs.addPropertyValue((String)entry.getKey(), entry.getValue());
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ExtendedServletRequestDataBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */