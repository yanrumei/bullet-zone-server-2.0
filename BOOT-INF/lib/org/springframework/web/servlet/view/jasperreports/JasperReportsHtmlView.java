/*    */ package org.springframework.web.servlet.view.jasperreports;
/*    */ 
/*    */ import net.sf.jasperreports.engine.JRExporter;
/*    */ import net.sf.jasperreports.engine.export.JRHtmlExporter;
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
/*    */ public class JasperReportsHtmlView
/*    */   extends AbstractJasperReportsSingleFormatView
/*    */ {
/*    */   public JasperReportsHtmlView()
/*    */   {
/* 35 */     setContentType("text/html");
/*    */   }
/*    */   
/*    */   protected JRExporter createExporter()
/*    */   {
/* 40 */     return new JRHtmlExporter();
/*    */   }
/*    */   
/*    */   protected boolean useWriter()
/*    */   {
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\JasperReportsHtmlView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */