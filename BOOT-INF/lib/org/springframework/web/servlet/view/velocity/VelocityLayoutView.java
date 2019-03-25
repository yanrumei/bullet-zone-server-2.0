/*     */ package org.springframework.web.servlet.view.velocity;
/*     */ 
/*     */ import java.io.StringWriter;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.velocity.Template;
/*     */ import org.apache.velocity.context.Context;
/*     */ import org.apache.velocity.exception.ResourceNotFoundException;
/*     */ import org.springframework.core.NestedIOException;
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
/*     */ public class VelocityLayoutView
/*     */   extends VelocityToolboxView
/*     */ {
/*     */   public static final String DEFAULT_LAYOUT_URL = "layout.vm";
/*     */   public static final String DEFAULT_LAYOUT_KEY = "layout";
/*     */   public static final String DEFAULT_SCREEN_CONTENT_KEY = "screen_content";
/*  74 */   private String layoutUrl = "layout.vm";
/*     */   
/*  76 */   private String layoutKey = "layout";
/*     */   
/*  78 */   private String screenContentKey = "screen_content";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLayoutUrl(String layoutUrl)
/*     */   {
/*  87 */     this.layoutUrl = layoutUrl;
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
/*     */   public void setLayoutKey(String layoutKey)
/*     */   {
/* 101 */     this.layoutKey = layoutKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScreenContentKey(String screenContentKey)
/*     */   {
/* 113 */     this.screenContentKey = screenContentKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean checkResource(Locale locale)
/*     */     throws Exception
/*     */   {
/* 125 */     if (!super.checkResource(locale)) {
/* 126 */       return false;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 131 */       getTemplate(this.layoutUrl);
/* 132 */       return true;
/*     */     }
/*     */     catch (ResourceNotFoundException ex) {
/* 135 */       throw new NestedIOException("Cannot find Velocity template for URL [" + this.layoutUrl + "]: Did you specify the correct resource loader path?", ex);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 139 */       throw new NestedIOException("Could not load Velocity template for URL [" + this.layoutUrl + "]", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doRender(Context context, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 152 */     renderScreenContent(context);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */     String layoutUrlToUse = (String)context.get(this.layoutKey);
/* 159 */     if (layoutUrlToUse != null) {
/* 160 */       if (this.logger.isDebugEnabled()) {
/* 161 */         this.logger.debug("Screen content template has requested layout [" + layoutUrlToUse + "]");
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 166 */       layoutUrlToUse = this.layoutUrl;
/*     */     }
/*     */     
/* 169 */     mergeTemplate(getTemplate(layoutUrlToUse), context, response);
/*     */   }
/*     */   
/*     */ 
/*     */   private void renderScreenContent(Context velocityContext)
/*     */     throws Exception
/*     */   {
/* 176 */     if (this.logger.isDebugEnabled()) {
/* 177 */       this.logger.debug("Rendering screen content template [" + getUrl() + "]");
/*     */     }
/*     */     
/* 180 */     StringWriter sw = new StringWriter();
/* 181 */     Template screenContentTemplate = getTemplate(getUrl());
/* 182 */     screenContentTemplate.merge(velocityContext, sw);
/*     */     
/*     */ 
/* 185 */     velocityContext.put(this.screenContentKey, sw.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\velocity\VelocityLayoutView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */