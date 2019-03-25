/*     */ package org.springframework.boot.system;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainer;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
/*     */ import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmbeddedServerPortFileWriter
/*     */   implements ApplicationListener<EmbeddedServletContainerInitializedEvent>
/*     */ {
/*     */   private static final String DEFAULT_FILE_NAME = "application.port";
/*  47 */   private static final String[] PROPERTY_VARIABLES = { "PORTFILE", "portfile" };
/*     */   
/*     */ 
/*  50 */   private static final Log logger = LogFactory.getLog(EmbeddedServerPortFileWriter.class);
/*     */   
/*     */ 
/*     */   private final File file;
/*     */   
/*     */ 
/*     */ 
/*     */   public EmbeddedServerPortFileWriter()
/*     */   {
/*  59 */     this(new File("application.port"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EmbeddedServerPortFileWriter(String filename)
/*     */   {
/*  68 */     this(new File(filename));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EmbeddedServerPortFileWriter(File file)
/*     */   {
/*  76 */     Assert.notNull(file, "File must not be null");
/*  77 */     String override = SystemProperties.get(PROPERTY_VARIABLES);
/*  78 */     if (override != null) {
/*  79 */       this.file = new File(override);
/*     */     }
/*     */     else {
/*  82 */       this.file = file;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event)
/*     */   {
/*  88 */     File portFile = getPortFile(event.getApplicationContext());
/*     */     try {
/*  90 */       String port = String.valueOf(event.getEmbeddedServletContainer().getPort());
/*  91 */       createParentFolder(portFile);
/*  92 */       FileCopyUtils.copy(port.getBytes(), portFile);
/*  93 */       portFile.deleteOnExit();
/*     */     }
/*     */     catch (Exception ex) {
/*  96 */       logger.warn(String.format("Cannot create port file %s", new Object[] { this.file }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected File getPortFile(EmbeddedWebApplicationContext applicationContext)
/*     */   {
/* 108 */     String contextName = applicationContext.getNamespace();
/* 109 */     if (StringUtils.isEmpty(contextName)) {
/* 110 */       return this.file;
/*     */     }
/* 112 */     String name = this.file.getName();
/* 113 */     String extension = StringUtils.getFilenameExtension(this.file.getName());
/* 114 */     name = name.substring(0, name.length() - extension.length() - 1);
/* 115 */     if (isUpperCase(name)) {
/* 116 */       name = name + "-" + contextName.toUpperCase();
/*     */     }
/*     */     else {
/* 119 */       name = name + "-" + contextName.toLowerCase();
/*     */     }
/* 121 */     if (StringUtils.hasLength(extension)) {
/* 122 */       name = name + "." + extension;
/*     */     }
/* 124 */     return new File(this.file.getParentFile(), name);
/*     */   }
/*     */   
/*     */   private boolean isUpperCase(String name) {
/* 128 */     for (int i = 0; i < name.length(); i++) {
/* 129 */       if ((Character.isLetter(name.charAt(i))) && 
/* 130 */         (!Character.isUpperCase(name.charAt(i)))) {
/* 131 */         return false;
/*     */       }
/*     */     }
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   private void createParentFolder(File file) {
/* 138 */     File parent = file.getParentFile();
/* 139 */     if (parent != null) {
/* 140 */       parent.mkdirs();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\system\EmbeddedServerPortFileWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */