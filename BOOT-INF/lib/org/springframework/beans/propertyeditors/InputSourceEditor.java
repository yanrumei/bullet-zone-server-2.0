/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceEditor;
/*    */ import org.springframework.util.Assert;
/*    */ import org.xml.sax.InputSource;
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
/*    */ public class InputSourceEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final ResourceEditor resourceEditor;
/*    */   
/*    */   public InputSourceEditor()
/*    */   {
/* 53 */     this.resourceEditor = new ResourceEditor();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public InputSourceEditor(ResourceEditor resourceEditor)
/*    */   {
/* 62 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/* 63 */     this.resourceEditor = resourceEditor;
/*    */   }
/*    */   
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 69 */     this.resourceEditor.setAsText(text);
/* 70 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*    */     try {
/* 72 */       setValue(resource != null ? new InputSource(resource.getURL().toString()) : null);
/*    */     }
/*    */     catch (IOException ex)
/*    */     {
/* 76 */       throw new IllegalArgumentException("Could not retrieve URL for " + resource + ": " + ex.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */   public String getAsText()
/*    */   {
/* 82 */     InputSource value = (InputSource)getValue();
/* 83 */     return value != null ? value.getSystemId() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\InputSourceEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */