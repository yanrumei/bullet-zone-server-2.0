/*    */ package org.springframework.web.servlet.view.jasperreports;
/*    */ 
/*    */ import net.sf.jasperreports.engine.JRExporter;
/*    */ import org.springframework.beans.BeanUtils;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigurableJasperReportsView
/*    */   extends AbstractJasperReportsSingleFormatView
/*    */ {
/*    */   private Class<? extends JRExporter> exporterClass;
/* 42 */   private boolean useWriter = true;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setExporterClass(Class<? extends JRExporter> exporterClass)
/*    */   {
/* 51 */     Assert.isAssignable(JRExporter.class, exporterClass);
/* 52 */     this.exporterClass = exporterClass;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setUseWriter(boolean useWriter)
/*    */   {
/* 61 */     this.useWriter = useWriter;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void onInit()
/*    */   {
/* 69 */     if (this.exporterClass == null) {
/* 70 */       throw new IllegalArgumentException("exporterClass is required");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected JRExporter createExporter()
/*    */   {
/* 82 */     return (JRExporter)BeanUtils.instantiateClass(this.exporterClass);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean useWriter()
/*    */   {
/* 91 */     return this.useWriter;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\ConfigurableJasperReportsView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */