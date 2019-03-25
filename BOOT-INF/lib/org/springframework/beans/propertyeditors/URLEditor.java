/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceEditor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class URLEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final ResourceEditor resourceEditor;
/*    */   
/*    */   public URLEditor()
/*    */   {
/* 56 */     this.resourceEditor = new ResourceEditor();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public URLEditor(ResourceEditor resourceEditor)
/*    */   {
/* 64 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/* 65 */     this.resourceEditor = resourceEditor;
/*    */   }
/*    */   
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 71 */     this.resourceEditor.setAsText(text);
/* 72 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*    */     try {
/* 74 */       setValue(resource != null ? resource.getURL() : null);
/*    */     }
/*    */     catch (IOException ex) {
/* 77 */       throw new IllegalArgumentException("Could not retrieve URL for " + resource + ": " + ex.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */   public String getAsText()
/*    */   {
/* 83 */     URL value = (URL)getValue();
/* 84 */     return value != null ? value.toExternalForm() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\URLEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */