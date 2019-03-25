/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final ResourceEditor resourceEditor;
/*     */   
/*     */   public FileEditor()
/*     */   {
/*  65 */     this.resourceEditor = new ResourceEditor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileEditor(ResourceEditor resourceEditor)
/*     */   {
/*  73 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/*  74 */     this.resourceEditor = resourceEditor;
/*     */   }
/*     */   
/*     */   public void setAsText(String text)
/*     */     throws IllegalArgumentException
/*     */   {
/*  80 */     if (!StringUtils.hasText(text)) {
/*  81 */       setValue(null);
/*  82 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  87 */     File file = null;
/*  88 */     if (!ResourceUtils.isUrl(text)) {
/*  89 */       file = new File(text);
/*  90 */       if (file.isAbsolute()) {
/*  91 */         setValue(file);
/*  92 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  97 */     this.resourceEditor.setAsText(text);
/*  98 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*     */     
/*     */ 
/* 101 */     if ((file == null) || (resource.exists())) {
/*     */       try {
/* 103 */         setValue(resource.getFile());
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 107 */         throw new IllegalArgumentException("Could not retrieve file for " + resource + ": " + ex.getMessage());
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 112 */       setValue(file);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getAsText()
/*     */   {
/* 118 */     File value = (File)getValue();
/* 119 */     return value != null ? value.getPath() : "";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\FileEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */