/*     */ package org.springframework.web.servlet.view.jasperreports;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import net.sf.jasperreports.engine.JasperPrint;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public class JasperReportsMultiFormatView
/*     */   extends AbstractJasperReportsView
/*     */ {
/*     */   public static final String DEFAULT_FORMAT_KEY = "format";
/*  80 */   private String formatKey = "format";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, Class<? extends AbstractJasperReportsView>> formatMappings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Properties contentDispositionMappings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JasperReportsMultiFormatView()
/*     */   {
/*  99 */     this.formatMappings = new HashMap(4);
/* 100 */     this.formatMappings.put("csv", JasperReportsCsvView.class);
/* 101 */     this.formatMappings.put("html", JasperReportsHtmlView.class);
/* 102 */     this.formatMappings.put("pdf", JasperReportsPdfView.class);
/* 103 */     this.formatMappings.put("xls", JasperReportsXlsView.class);
/* 104 */     this.formatMappings.put("xlsx", JasperReportsXlsxView.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFormatKey(String formatKey)
/*     */   {
/* 113 */     this.formatKey = formatKey;
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
/*     */   public void setFormatMappings(Map<String, Class<? extends AbstractJasperReportsView>> formatMappings)
/*     */   {
/* 128 */     if (CollectionUtils.isEmpty(formatMappings)) {
/* 129 */       throw new IllegalArgumentException("'formatMappings' must not be empty");
/*     */     }
/* 131 */     this.formatMappings = formatMappings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentDispositionMappings(Properties mappings)
/*     */   {
/* 141 */     this.contentDispositionMappings = mappings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Properties getContentDispositionMappings()
/*     */   {
/* 150 */     if (this.contentDispositionMappings == null) {
/* 151 */       this.contentDispositionMappings = new Properties();
/*     */     }
/* 153 */     return this.contentDispositionMappings;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean generatesDownloadContent()
/*     */   {
/* 159 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 171 */     String format = (String)model.get(this.formatKey);
/* 172 */     if (format == null) {
/* 173 */       throw new IllegalArgumentException("No format found in model");
/*     */     }
/*     */     
/* 176 */     if (this.logger.isDebugEnabled()) {
/* 177 */       this.logger.debug("Rendering report using format mapping key [" + format + "]");
/*     */     }
/*     */     
/* 180 */     Class<? extends AbstractJasperReportsView> viewClass = (Class)this.formatMappings.get(format);
/* 181 */     if (viewClass == null) {
/* 182 */       throw new IllegalArgumentException("Format discriminator [" + format + "] is not a configured mapping");
/*     */     }
/*     */     
/* 185 */     if (this.logger.isDebugEnabled()) {
/* 186 */       this.logger.debug("Rendering report using view class [" + viewClass.getName() + "]");
/*     */     }
/*     */     
/* 189 */     AbstractJasperReportsView view = (AbstractJasperReportsView)BeanUtils.instantiateClass(viewClass);
/*     */     
/*     */ 
/* 192 */     view.setExporterParameters(getExporterParameters());
/* 193 */     view.setConvertedExporterParameters(getConvertedExporterParameters());
/*     */     
/*     */ 
/* 196 */     populateContentDispositionIfNecessary(response, format);
/* 197 */     view.renderReport(populatedReport, model, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void populateContentDispositionIfNecessary(HttpServletResponse response, String format)
/*     */   {
/* 208 */     if (this.contentDispositionMappings != null) {
/* 209 */       String header = this.contentDispositionMappings.getProperty(format);
/* 210 */       if (header != null) {
/* 211 */         if (this.logger.isDebugEnabled()) {
/* 212 */           this.logger.debug("Setting Content-Disposition header to: [" + header + "]");
/*     */         }
/* 214 */         response.setHeader("Content-Disposition", header);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\JasperReportsMultiFormatView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */