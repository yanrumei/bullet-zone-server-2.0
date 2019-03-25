/*     */ package org.springframework.web.servlet.view.velocity;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.velocity.VelocityContext;
/*     */ import org.apache.velocity.context.Context;
/*     */ import org.apache.velocity.tools.view.ToolboxManager;
/*     */ import org.apache.velocity.tools.view.context.ChainedContext;
/*     */ import org.apache.velocity.tools.view.servlet.ServletToolboxManager;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class VelocityToolboxView
/*     */   extends VelocityView
/*     */ {
/*     */   private String toolboxConfigLocation;
/*     */   
/*     */   public void setToolboxConfigLocation(String toolboxConfigLocation)
/*     */   {
/*  84 */     this.toolboxConfigLocation = toolboxConfigLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getToolboxConfigLocation()
/*     */   {
/*  91 */     return this.toolboxConfigLocation;
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
/*     */   protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 107 */     ChainedContext velocityContext = new ChainedContext(new VelocityContext(model), getVelocityEngine(), request, response, getServletContext());
/*     */     
/*     */ 
/* 110 */     if (getToolboxConfigLocation() != null) {
/* 111 */       ToolboxManager toolboxManager = ServletToolboxManager.getInstance(
/* 112 */         getServletContext(), getToolboxConfigLocation());
/* 113 */       Map<?, ?> toolboxContext = toolboxManager.getToolbox(velocityContext);
/* 114 */       velocityContext.setToolbox(toolboxContext);
/*     */     }
/*     */     
/* 117 */     return velocityContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initTool(Object tool, Context velocityContext)
/*     */     throws Exception
/*     */   {
/* 128 */     Method initMethod = ClassUtils.getMethodIfAvailable(tool.getClass(), "init", new Class[] { Object.class });
/* 129 */     if (initMethod != null) {
/* 130 */       ReflectionUtils.invokeMethod(initMethod, tool, new Object[] { velocityContext });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\velocity\VelocityToolboxView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */