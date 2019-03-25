/*    */ package org.springframework.web.servlet.view.jasperreports;
/*    */ 
/*    */ import net.sf.jasperreports.engine.JRExporter;
/*    */ import net.sf.jasperreports.engine.export.JRCsvExporter;
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
/*    */ public class JasperReportsCsvView
/*    */   extends AbstractJasperReportsSingleFormatView
/*    */ {
/*    */   public JasperReportsCsvView()
/*    */   {
/* 37 */     setContentType("text/csv");
/*    */   }
/*    */   
/*    */   protected JRExporter createExporter()
/*    */   {
/* 42 */     return new JRCsvExporter();
/*    */   }
/*    */   
/*    */   protected boolean useWriter()
/*    */   {
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\JasperReportsCsvView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */