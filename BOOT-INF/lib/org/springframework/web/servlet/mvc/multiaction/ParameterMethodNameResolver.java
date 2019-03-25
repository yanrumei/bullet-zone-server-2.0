/*     */ package org.springframework.web.servlet.mvc.multiaction;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ParameterMethodNameResolver
/*     */   implements MethodNameResolver
/*     */ {
/*     */   public static final String DEFAULT_PARAM_NAME = "action";
/*  94 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  96 */   private String paramName = "action";
/*     */   
/*     */ 
/*     */ 
/*     */   private String[] methodParamNames;
/*     */   
/*     */ 
/*     */ 
/*     */   private Properties logicalMappings;
/*     */   
/*     */ 
/*     */ 
/*     */   private String defaultMethodName;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setParamName(String paramName)
/*     */   {
/* 114 */     if (paramName != null) {
/* 115 */       Assert.hasText(paramName, "'paramName' must not be empty");
/*     */     }
/* 117 */     this.paramName = paramName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMethodParamNames(String... methodParamNames)
/*     */   {
/* 129 */     this.methodParamNames = methodParamNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLogicalMappings(Properties logicalMappings)
/*     */   {
/* 146 */     this.logicalMappings = logicalMappings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultMethodName(String defaultMethodName)
/*     */   {
/* 154 */     if (defaultMethodName != null) {
/* 155 */       Assert.hasText(defaultMethodName, "'defaultMethodName' must not be empty");
/*     */     }
/* 157 */     this.defaultMethodName = defaultMethodName;
/*     */   }
/*     */   
/*     */   public String getHandlerMethodName(HttpServletRequest request)
/*     */     throws NoSuchRequestHandlingMethodException
/*     */   {
/* 163 */     String methodName = null;
/*     */     
/*     */ 
/*     */ 
/* 167 */     if (this.methodParamNames != null) {
/* 168 */       for (String candidate : this.methodParamNames) {
/* 169 */         if (WebUtils.hasSubmitParameter(request, candidate)) {
/* 170 */           methodName = candidate;
/* 171 */           if (!this.logger.isDebugEnabled()) break;
/* 172 */           this.logger.debug("Determined handler method '" + methodName + "' based on existence of explicit request parameter of same name"); break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */     if ((methodName == null) && (this.paramName != null)) {
/* 182 */       methodName = request.getParameter(this.paramName);
/* 183 */       if ((methodName != null) && 
/* 184 */         (this.logger.isDebugEnabled())) {
/* 185 */         this.logger.debug("Determined handler method '" + methodName + "' based on value of request parameter '" + this.paramName + "'");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 191 */     if ((methodName != null) && (this.logicalMappings != null))
/*     */     {
/* 193 */       String originalName = methodName;
/* 194 */       methodName = this.logicalMappings.getProperty(methodName, methodName);
/* 195 */       if (this.logger.isDebugEnabled()) {
/* 196 */         this.logger.debug("Resolved method name '" + originalName + "' to handler method '" + methodName + "'");
/*     */       }
/*     */     }
/*     */     
/* 200 */     if ((methodName != null) && (!StringUtils.hasText(methodName))) {
/* 201 */       if (this.logger.isDebugEnabled()) {
/* 202 */         this.logger.debug("Method name '" + methodName + "' is empty: treating it as no method name found");
/*     */       }
/* 204 */       methodName = null;
/*     */     }
/*     */     
/* 207 */     if (methodName == null) {
/* 208 */       if (this.defaultMethodName != null)
/*     */       {
/* 210 */         methodName = this.defaultMethodName;
/* 211 */         if (this.logger.isDebugEnabled()) {
/* 212 */           this.logger.debug("Falling back to default handler method '" + this.defaultMethodName + "'");
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 217 */         throw new NoSuchRequestHandlingMethodException(request);
/*     */       }
/*     */     }
/*     */     
/* 221 */     return methodName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\multiaction\ParameterMethodNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */