/*    */ package org.springframework.web.servlet.view.jasperreports;
/*    */ 
/*    */ import net.sf.jasperreports.engine.JRExporter;
/*    */ import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
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
/*    */ public class JasperReportsXlsxView
/*    */   extends AbstractJasperReportsSingleFormatView
/*    */ {
/*    */   public JasperReportsXlsxView()
/*    */   {
/* 37 */     setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*    */   }
/*    */   
/*    */   protected JRExporter createExporter()
/*    */   {
/* 42 */     return new JRXlsxExporter();
/*    */   }
/*    */   
/*    */   protected boolean useWriter()
/*    */   {
/* 47 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\JasperReportsXlsxView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */