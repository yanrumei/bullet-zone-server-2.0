/*    */ package org.apache.tomcat.util.descriptor.web;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import javax.servlet.descriptor.JspConfigDescriptor;
/*    */ import javax.servlet.descriptor.JspPropertyGroupDescriptor;
/*    */ import javax.servlet.descriptor.TaglibDescriptor;
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
/*    */ public class JspConfigDescriptorImpl
/*    */   implements JspConfigDescriptor
/*    */ {
/*    */   private final Collection<JspPropertyGroupDescriptor> jspPropertyGroups;
/*    */   private final Collection<TaglibDescriptor> taglibs;
/*    */   
/*    */   public JspConfigDescriptorImpl(Collection<JspPropertyGroupDescriptor> jspPropertyGroups, Collection<TaglibDescriptor> taglibs)
/*    */   {
/* 33 */     this.jspPropertyGroups = jspPropertyGroups;
/* 34 */     this.taglibs = taglibs;
/*    */   }
/*    */   
/*    */   public Collection<JspPropertyGroupDescriptor> getJspPropertyGroups()
/*    */   {
/* 39 */     return new ArrayList(this.jspPropertyGroups);
/*    */   }
/*    */   
/*    */   public Collection<TaglibDescriptor> getTaglibs()
/*    */   {
/* 44 */     return new ArrayList(this.taglibs);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\JspConfigDescriptorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */