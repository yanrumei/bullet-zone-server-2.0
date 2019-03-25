/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.FileSystemNotFoundException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.util.Assert;
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
/*     */ @UsesJava7
/*     */ public class PathEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final ResourceEditor resourceEditor;
/*     */   
/*     */   public PathEditor()
/*     */   {
/*  64 */     this.resourceEditor = new ResourceEditor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathEditor(ResourceEditor resourceEditor)
/*     */   {
/*  72 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/*  73 */     this.resourceEditor = resourceEditor;
/*     */   }
/*     */   
/*     */   public void setAsText(String text)
/*     */     throws IllegalArgumentException
/*     */   {
/*  79 */     boolean nioPathCandidate = !text.startsWith("classpath:");
/*  80 */     if ((nioPathCandidate) && (!text.startsWith("/"))) {
/*     */       try {
/*  82 */         URI uri = new URI(text);
/*  83 */         if (uri.getScheme() != null) {
/*  84 */           nioPathCandidate = false;
/*     */           
/*  86 */           setValue(Paths.get(uri).normalize());
/*  87 */           return;
/*     */         }
/*     */       }
/*     */       catch (URISyntaxException localURISyntaxException) {}catch (FileSystemNotFoundException localFileSystemNotFoundException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */     this.resourceEditor.setAsText(text);
/* 100 */     Resource resource = (Resource)this.resourceEditor.getValue();
/* 101 */     if (resource == null) {
/* 102 */       setValue(null);
/*     */     }
/* 104 */     else if ((!resource.exists()) && (nioPathCandidate)) {
/* 105 */       setValue(Paths.get(text, new String[0]).normalize());
/*     */     } else {
/*     */       try
/*     */       {
/* 109 */         setValue(resource.getFile().toPath());
/*     */       }
/*     */       catch (IOException ex) {
/* 112 */         throw new IllegalArgumentException("Failed to retrieve file for " + resource, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getAsText()
/*     */   {
/* 119 */     Path value = (Path)getValue();
/* 120 */     return value != null ? value.toString() : "";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\PathEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */