/*     */ package org.springframework.web.servlet.view.jasperreports;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JasperReportsViewResolver
/*     */   extends UrlBasedViewResolver
/*     */ {
/*     */   private String reportDataKey;
/*     */   private Properties subReportUrls;
/*     */   private String[] subReportDataKeys;
/*     */   private Properties headers;
/*  45 */   private Map<String, Object> exporterParameters = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   private DataSource jdbcDataSource;
/*     */   
/*     */ 
/*     */ 
/*     */   protected Class<?> requiredViewClass()
/*     */   {
/*  55 */     return AbstractJasperReportsView.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReportDataKey(String reportDataKey)
/*     */   {
/*  63 */     this.reportDataKey = reportDataKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSubReportUrls(Properties subReportUrls)
/*     */   {
/*  71 */     this.subReportUrls = subReportUrls;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSubReportDataKeys(String... subReportDataKeys)
/*     */   {
/*  79 */     this.subReportDataKeys = subReportDataKeys;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeaders(Properties headers)
/*     */   {
/*  87 */     this.headers = headers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExporterParameters(Map<String, Object> exporterParameters)
/*     */   {
/*  95 */     this.exporterParameters = exporterParameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJdbcDataSource(DataSource jdbcDataSource)
/*     */   {
/* 103 */     this.jdbcDataSource = jdbcDataSource;
/*     */   }
/*     */   
/*     */   protected AbstractUrlBasedView buildView(String viewName)
/*     */     throws Exception
/*     */   {
/* 109 */     AbstractJasperReportsView view = (AbstractJasperReportsView)super.buildView(viewName);
/* 110 */     view.setReportDataKey(this.reportDataKey);
/* 111 */     view.setSubReportUrls(this.subReportUrls);
/* 112 */     view.setSubReportDataKeys(this.subReportDataKeys);
/* 113 */     view.setHeaders(this.headers);
/* 114 */     view.setExporterParameters(this.exporterParameters);
/* 115 */     view.setJdbcDataSource(this.jdbcDataSource);
/* 116 */     return view;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\JasperReportsViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */