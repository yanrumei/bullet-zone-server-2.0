/*    */ package org.springframework.web.servlet.view.jasperreports;
/*    */ 
/*    */ import net.sf.jasperreports.engine.JRExporter;
/*    */ import net.sf.jasperreports.engine.export.JRXlsExporter;
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
/*    */ public class JasperReportsXlsView
/*    */   extends AbstractJasperReportsSingleFormatView
/*    */ {
/*    */   public JasperReportsXlsView()
/*    */   {
/* 37 */     setContentType("application/vnd.ms-excel");
/*    */   }
/*    */   
/*    */   protected JRExporter createExporter()
/*    */   {
/* 42 */     return new JRXlsExporter();
/*    */   }
/*    */   
/*    */   protected boolean useWriter()
/*    */   {
/* 47 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\JasperReportsXlsView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */