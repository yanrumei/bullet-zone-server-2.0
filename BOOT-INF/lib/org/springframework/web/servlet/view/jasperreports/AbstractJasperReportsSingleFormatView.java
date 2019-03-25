/*     */ package org.springframework.web.servlet.view.jasperreports;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import net.sf.jasperreports.engine.JRExporter;
/*     */ import net.sf.jasperreports.engine.JRExporterParameter;
/*     */ import net.sf.jasperreports.engine.JasperPrint;
/*     */ import org.springframework.ui.jasperreports.JasperReportsUtils;
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
/*     */ public abstract class AbstractJasperReportsSingleFormatView
/*     */   extends AbstractJasperReportsView
/*     */ {
/*     */   protected boolean generatesDownloadContent()
/*     */   {
/*  52 */     return !useWriter();
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
/*  64 */     JRExporter exporter = createExporter();
/*     */     
/*  66 */     Map<JRExporterParameter, Object> mergedExporterParameters = getConvertedExporterParameters();
/*  67 */     if (!CollectionUtils.isEmpty(mergedExporterParameters)) {
/*  68 */       exporter.setParameters(mergedExporterParameters);
/*     */     }
/*     */     
/*  71 */     if (useWriter()) {
/*  72 */       renderReportUsingWriter(exporter, populatedReport, response);
/*     */     }
/*     */     else {
/*  75 */       renderReportUsingOutputStream(exporter, populatedReport, response);
/*     */     }
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
/*     */   protected void renderReportUsingWriter(JRExporter exporter, JasperPrint populatedReport, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  90 */     String contentType = getContentType();
/*  91 */     String encoding = (String)exporter.getParameter(JRExporterParameter.CHARACTER_ENCODING);
/*  92 */     if (encoding != null)
/*     */     {
/*  94 */       if ((contentType != null) && (!contentType.toLowerCase().contains(";charset="))) {
/*  95 */         contentType = contentType + ";charset=" + encoding;
/*     */       }
/*     */     }
/*  98 */     response.setContentType(contentType);
/*     */     
/*     */ 
/* 101 */     JasperReportsUtils.render(exporter, populatedReport, response.getWriter());
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
/*     */   protected void renderReportUsingOutputStream(JRExporter exporter, JasperPrint populatedReport, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 115 */     ByteArrayOutputStream baos = createTemporaryOutputStream();
/* 116 */     JasperReportsUtils.render(exporter, populatedReport, baos);
/* 117 */     writeToResponse(response, baos);
/*     */   }
/*     */   
/*     */   protected abstract JRExporter createExporter();
/*     */   
/*     */   protected abstract boolean useWriter();
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\AbstractJasperReportsSingleFormatView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */