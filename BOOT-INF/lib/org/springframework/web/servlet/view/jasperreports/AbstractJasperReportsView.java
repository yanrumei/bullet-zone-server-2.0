/*     */ package org.springframework.web.servlet.view.jasperreports;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.sql.Connection;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.sql.DataSource;
/*     */ import net.sf.jasperreports.engine.JRDataSource;
/*     */ import net.sf.jasperreports.engine.JRDataSourceProvider;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JRExporterParameter;
/*     */ import net.sf.jasperreports.engine.JasperCompileManager;
/*     */ import net.sf.jasperreports.engine.JasperFillManager;
/*     */ import net.sf.jasperreports.engine.JasperPrint;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.design.JasperDesign;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import net.sf.jasperreports.engine.xml.JRXmlLoader;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.support.MessageSourceResourceBundle;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.ui.jasperreports.JasperReportsUtils;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
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
/*     */ public abstract class AbstractJasperReportsView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   protected static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
/*     */   protected static final String CONTENT_DISPOSITION_INLINE = "inline";
/*     */   private String reportDataKey;
/*     */   private Properties subReportUrls;
/*     */   private String[] subReportDataKeys;
/*     */   private Properties headers;
/* 159 */   private Map<?, ?> exporterParameters = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<JRExporterParameter, Object> convertedExporterParameters;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private DataSource jdbcDataSource;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JasperReport report;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, JasperReport> subReports;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReportDataKey(String reportDataKey)
/*     */   {
/* 199 */     this.reportDataKey = reportDataKey;
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
/*     */   public void setSubReportUrls(Properties subReports)
/*     */   {
/* 212 */     this.subReportUrls = subReports;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSubReportDataKeys(String... subReportDataKeys)
/*     */   {
/* 236 */     this.subReportDataKeys = subReportDataKeys;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeaders(Properties headers)
/*     */   {
/* 244 */     this.headers = headers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExporterParameters(Map<?, ?> parameters)
/*     */   {
/* 255 */     this.exporterParameters = parameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<?, ?> getExporterParameters()
/*     */   {
/* 262 */     return this.exporterParameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void setConvertedExporterParameters(Map<JRExporterParameter, Object> parameters)
/*     */   {
/* 269 */     this.convertedExporterParameters = parameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Map<JRExporterParameter, Object> getConvertedExporterParameters()
/*     */   {
/* 276 */     return this.convertedExporterParameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJdbcDataSource(DataSource jdbcDataSource)
/*     */   {
/* 284 */     this.jdbcDataSource = jdbcDataSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected DataSource getJdbcDataSource()
/*     */   {
/* 291 */     return this.jdbcDataSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isUrlRequired()
/*     */   {
/* 301 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void initApplicationContext()
/*     */     throws ApplicationContextException
/*     */   {
/* 312 */     this.report = loadReport();
/*     */     
/*     */     Enumeration<?> urls;
/* 315 */     if (this.subReportUrls != null) {
/* 316 */       if ((this.subReportDataKeys != null) && (this.subReportDataKeys.length > 0) && (this.reportDataKey == null)) {
/* 317 */         throw new ApplicationContextException("'reportDataKey' for main report is required when specifying a value for 'subReportDataKeys'");
/*     */       }
/*     */       
/* 320 */       this.subReports = new HashMap(this.subReportUrls.size());
/* 321 */       for (urls = this.subReportUrls.propertyNames(); urls.hasMoreElements();) {
/* 322 */         String key = (String)urls.nextElement();
/* 323 */         String path = this.subReportUrls.getProperty(key);
/* 324 */         Resource resource = getApplicationContext().getResource(path);
/* 325 */         this.subReports.put(key, loadReport(resource));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 330 */     convertExporterParameters();
/*     */     
/* 332 */     if (this.headers == null) {
/* 333 */       this.headers = new Properties();
/*     */     }
/* 335 */     if (!this.headers.containsKey("Content-Disposition")) {
/* 336 */       this.headers.setProperty("Content-Disposition", "inline");
/*     */     }
/*     */     
/* 339 */     onInit();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onInit() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void convertExporterParameters()
/*     */   {
/* 359 */     if (!CollectionUtils.isEmpty(this.exporterParameters))
/*     */     {
/* 361 */       this.convertedExporterParameters = new HashMap(this.exporterParameters.size());
/* 362 */       for (Map.Entry<?, ?> entry : this.exporterParameters.entrySet()) {
/* 363 */         JRExporterParameter exporterParameter = getExporterParameter(entry.getKey());
/* 364 */         this.convertedExporterParameters.put(exporterParameter, 
/* 365 */           convertParameterValue(exporterParameter, entry.getValue()));
/*     */       }
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
/*     */ 
/*     */ 
/*     */   protected Object convertParameterValue(JRExporterParameter parameter, Object value)
/*     */   {
/* 382 */     if ((value instanceof String)) {
/* 383 */       String str = (String)value;
/* 384 */       if ("true".equals(str)) {
/* 385 */         return Boolean.TRUE;
/*     */       }
/* 387 */       if ("false".equals(str)) {
/* 388 */         return Boolean.FALSE;
/*     */       }
/* 390 */       if ((str.length() > 0) && (Character.isDigit(str.charAt(0)))) {
/*     */         try
/*     */         {
/* 393 */           return Integer.valueOf(str);
/*     */         }
/*     */         catch (NumberFormatException ex)
/*     */         {
/* 397 */           return str;
/*     */         }
/*     */       }
/*     */     }
/* 401 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JRExporterParameter getExporterParameter(Object parameter)
/*     */   {
/* 412 */     if ((parameter instanceof JRExporterParameter)) {
/* 413 */       return (JRExporterParameter)parameter;
/*     */     }
/* 415 */     if ((parameter instanceof String)) {
/* 416 */       return convertToExporterParameter((String)parameter);
/*     */     }
/* 418 */     throw new IllegalArgumentException("Parameter [" + parameter + "] is invalid type. Should be either String or JRExporterParameter.");
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
/*     */   protected JRExporterParameter convertToExporterParameter(String fqFieldName)
/*     */   {
/* 431 */     int index = fqFieldName.lastIndexOf('.');
/* 432 */     if ((index == -1) || (index == fqFieldName.length())) {
/* 433 */       throw new IllegalArgumentException("Parameter name [" + fqFieldName + "] is not a valid static field. The parameter name must map to a static field such as [net.sf.jasperreports.engine.export.JRHtmlExporterParameter.IMAGES_URI]");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 438 */     String className = fqFieldName.substring(0, index);
/* 439 */     String fieldName = fqFieldName.substring(index + 1);
/*     */     try
/*     */     {
/* 442 */       Class<?> cls = ClassUtils.forName(className, getApplicationContext().getClassLoader());
/* 443 */       Field field = cls.getField(fieldName);
/*     */       
/* 445 */       if (JRExporterParameter.class.isAssignableFrom(field.getType())) {
/*     */         try {
/* 447 */           return (JRExporterParameter)field.get(null);
/*     */         }
/*     */         catch (IllegalAccessException ex) {
/* 450 */           throw new IllegalArgumentException("Unable to access field [" + fieldName + "] of class [" + className + "]. Check that it is static and accessible.");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 456 */       throw new IllegalArgumentException("Field [" + fieldName + "] on class [" + className + "] is not assignable from JRExporterParameter - check the type of this field.");
/*     */ 
/*     */     }
/*     */     catch (ClassNotFoundException ex)
/*     */     {
/* 461 */       throw new IllegalArgumentException("Class [" + className + "] in key [" + fqFieldName + "] could not be found.");
/*     */     }
/*     */     catch (NoSuchFieldException ex)
/*     */     {
/* 465 */       throw new IllegalArgumentException("Field [" + fieldName + "] in key [" + fqFieldName + "] could not be found on class [" + className + "].");
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
/*     */   protected JasperReport loadReport()
/*     */   {
/* 478 */     String url = getUrl();
/* 479 */     if (url == null) {
/* 480 */       return null;
/*     */     }
/* 482 */     Resource mainReport = getApplicationContext().getResource(url);
/* 483 */     return loadReport(mainReport);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JasperReport loadReport(Resource resource)
/*     */   {
/*     */     try
/*     */     {
/* 495 */       String filename = resource.getFilename();
/* 496 */       if (filename != null) {
/* 497 */         if (filename.endsWith(".jasper"))
/*     */         {
/* 499 */           if (this.logger.isInfoEnabled()) {
/* 500 */             this.logger.info("Loading pre-compiled Jasper Report from " + resource);
/*     */           }
/* 502 */           InputStream is = resource.getInputStream();
/*     */           try {
/* 504 */             return (JasperReport)JRLoader.loadObject(is);
/*     */           }
/*     */           finally {
/* 507 */             is.close();
/*     */           }
/*     */         }
/* 510 */         if (filename.endsWith(".jrxml"))
/*     */         {
/* 512 */           if (this.logger.isInfoEnabled()) {
/* 513 */             this.logger.info("Compiling Jasper Report loaded from " + resource);
/*     */           }
/* 515 */           InputStream is = resource.getInputStream();
/*     */           try {
/* 517 */             JasperDesign design = JRXmlLoader.load(is);
/* 518 */             return JasperCompileManager.compileReport(design);
/*     */           }
/*     */           finally {
/* 521 */             is.close();
/*     */           }
/*     */         }
/*     */       }
/* 525 */       throw new IllegalArgumentException("Report filename [" + filename + "] must end in either .jasper or .jrxml");
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 529 */       throw new ApplicationContextException("Could not load JasperReports report from " + resource, ex);
/*     */     }
/*     */     catch (JRException ex)
/*     */     {
/* 533 */       throw new ApplicationContextException("Could not parse JasperReports report from " + resource, ex);
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
/*     */ 
/*     */ 
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 550 */     if (this.subReports != null)
/*     */     {
/* 552 */       model.putAll(this.subReports);
/*     */       
/*     */ 
/* 555 */       if (this.subReportDataKeys != null) {
/* 556 */         for (String key : this.subReportDataKeys) {
/* 557 */           model.put(key, convertReportData(model.get(key)));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 563 */     exposeLocalizationContext(model, request);
/*     */     
/*     */ 
/* 566 */     JasperPrint filledReport = fillReport(model);
/* 567 */     postProcessReport(filledReport, model);
/*     */     
/*     */ 
/* 570 */     populateHeaders(response);
/* 571 */     renderReport(filledReport, model, response);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposeLocalizationContext(Map<String, Object> model, HttpServletRequest request)
/*     */   {
/* 589 */     RequestContext rc = new RequestContext(request, getServletContext());
/* 590 */     Locale locale = rc.getLocale();
/* 591 */     if (!model.containsKey("REPORT_LOCALE")) {
/* 592 */       model.put("REPORT_LOCALE", locale);
/*     */     }
/* 594 */     TimeZone timeZone = rc.getTimeZone();
/* 595 */     if ((timeZone != null) && (!model.containsKey("REPORT_TIME_ZONE"))) {
/* 596 */       model.put("REPORT_TIME_ZONE", timeZone);
/*     */     }
/* 598 */     JasperReport report = getReport();
/* 599 */     if (((report == null) || (report.getResourceBundle() == null)) && 
/* 600 */       (!model.containsKey("REPORT_RESOURCE_BUNDLE"))) {
/* 601 */       model.put("REPORT_RESOURCE_BUNDLE", new MessageSourceResourceBundle(rc
/* 602 */         .getMessageSource(), locale));
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
/*     */   protected JasperPrint fillReport(Map<String, Object> model)
/*     */     throws Exception
/*     */   {
/* 632 */     JasperReport report = getReport();
/* 633 */     if (report == null) {
/* 634 */       throw new IllegalStateException("No main report defined for 'fillReport' - specify a 'url' on this view or override 'getReport()' or 'fillReport(Map)'");
/*     */     }
/*     */     
/*     */ 
/* 638 */     JRDataSource jrDataSource = null;
/* 639 */     DataSource jdbcDataSourceToUse = null;
/*     */     
/*     */ 
/* 642 */     if (this.reportDataKey != null) {
/* 643 */       Object reportDataValue = model.get(this.reportDataKey);
/* 644 */       if ((reportDataValue instanceof DataSource)) {
/* 645 */         jdbcDataSourceToUse = (DataSource)reportDataValue;
/*     */       }
/*     */       else {
/* 648 */         jrDataSource = convertReportData(reportDataValue);
/*     */       }
/*     */     }
/*     */     else {
/* 652 */       Collection<?> values = model.values();
/* 653 */       jrDataSource = (JRDataSource)CollectionUtils.findValueOfType(values, JRDataSource.class);
/* 654 */       if (jrDataSource == null) {
/* 655 */         JRDataSourceProvider provider = (JRDataSourceProvider)CollectionUtils.findValueOfType(values, JRDataSourceProvider.class);
/* 656 */         if (provider != null) {
/* 657 */           jrDataSource = createReport(provider);
/*     */         }
/*     */         else {
/* 660 */           jdbcDataSourceToUse = (DataSource)CollectionUtils.findValueOfType(values, DataSource.class);
/* 661 */           if (jdbcDataSourceToUse == null) {
/* 662 */             jdbcDataSourceToUse = this.jdbcDataSource;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 668 */     if (jdbcDataSourceToUse != null) {
/* 669 */       return doFillReport(report, model, jdbcDataSourceToUse);
/*     */     }
/*     */     
/*     */ 
/* 673 */     if (jrDataSource == null) {
/* 674 */       jrDataSource = getReportData(model);
/*     */     }
/* 676 */     if (jrDataSource != null)
/*     */     {
/* 678 */       if (this.logger.isDebugEnabled()) {
/* 679 */         this.logger.debug("Filling report with JRDataSource [" + jrDataSource + "]");
/*     */       }
/* 681 */       return JasperFillManager.fillReport(report, model, jrDataSource);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 686 */     this.logger.debug("Filling report with plain model");
/* 687 */     return JasperFillManager.fillReport(report, model);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JasperPrint doFillReport(JasperReport report, Map<String, Object> model, DataSource ds)
/*     */     throws Exception
/*     */   {
/* 697 */     if (this.logger.isDebugEnabled()) {
/* 698 */       this.logger.debug("Filling report using JDBC DataSource [" + ds + "]");
/*     */     }
/* 700 */     Connection con = ds.getConnection();
/*     */     try {
/* 702 */       return JasperFillManager.fillReport(report, model, con);
/*     */     }
/*     */     finally {
/*     */       try {
/* 706 */         con.close();
/*     */       }
/*     */       catch (Throwable ex) {
/* 709 */         this.logger.debug("Could not close JDBC Connection", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void populateHeaders(HttpServletResponse response)
/*     */   {
/* 720 */     for (Enumeration<?> en = this.headers.propertyNames(); en.hasMoreElements();) {
/* 721 */       String key = (String)en.nextElement();
/* 722 */       response.addHeader(key, this.headers.getProperty(key));
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
/*     */ 
/*     */   protected JasperReport getReport()
/*     */   {
/* 737 */     return this.report;
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
/*     */   protected JRDataSource getReportData(Map<String, Object> model)
/*     */   {
/* 752 */     Object value = CollectionUtils.findValueOfType(model.values(), getReportDataTypes());
/* 753 */     return value != null ? convertReportData(value) : null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JRDataSource convertReportData(Object value)
/*     */     throws IllegalArgumentException
/*     */   {
/* 776 */     if ((value instanceof JRDataSourceProvider)) {
/* 777 */       return createReport((JRDataSourceProvider)value);
/*     */     }
/*     */     
/* 780 */     return JasperReportsUtils.convertReportData(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JRDataSource createReport(JRDataSourceProvider provider)
/*     */   {
/*     */     try
/*     */     {
/* 791 */       JasperReport report = getReport();
/* 792 */       if (report == null) {
/* 793 */         throw new IllegalStateException("No main report defined for JRDataSourceProvider - specify a 'url' on this view or override 'getReport()'");
/*     */       }
/*     */       
/* 796 */       return provider.create(report);
/*     */     }
/*     */     catch (JRException ex) {
/* 799 */       throw new IllegalArgumentException("Supplied JRDataSourceProvider is invalid", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?>[] getReportDataTypes()
/*     */   {
/* 811 */     return new Class[] { Collection.class, Object[].class };
/*     */   }
/*     */   
/*     */   protected void postProcessReport(JasperPrint populatedReport, Map<String, Object> model)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   protected abstract void renderReport(JasperPrint paramJasperPrint, Map<String, Object> paramMap, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\jasperreports\AbstractJasperReportsView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */