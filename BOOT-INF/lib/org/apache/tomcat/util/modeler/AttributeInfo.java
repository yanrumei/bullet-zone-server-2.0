/*     */ package org.apache.tomcat.util.modeler;
/*     */ 
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AttributeInfo
/*     */   extends FeatureInfo
/*     */ {
/*     */   static final long serialVersionUID = -2511626862303972143L;
/*  33 */   protected String displayName = null;
/*     */   
/*     */ 
/*  36 */   protected String getMethod = null;
/*  37 */   protected String setMethod = null;
/*  38 */   protected boolean readable = true;
/*  39 */   protected boolean writeable = true;
/*  40 */   protected boolean is = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/*  48 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {
/*  52 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getGetMethod()
/*     */   {
/*  59 */     if (this.getMethod == null)
/*  60 */       this.getMethod = getMethodName(getName(), true, isIs());
/*  61 */     return this.getMethod;
/*     */   }
/*     */   
/*     */   public void setGetMethod(String getMethod) {
/*  65 */     this.getMethod = getMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isIs()
/*     */   {
/*  74 */     return this.is;
/*     */   }
/*     */   
/*     */   public void setIs(boolean is) {
/*  78 */     this.is = is;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReadable()
/*     */   {
/*  87 */     return this.readable;
/*     */   }
/*     */   
/*     */   public void setReadable(boolean readable) {
/*  91 */     this.readable = readable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSetMethod()
/*     */   {
/*  99 */     if (this.setMethod == null)
/* 100 */       this.setMethod = getMethodName(getName(), false, false);
/* 101 */     return this.setMethod;
/*     */   }
/*     */   
/*     */   public void setSetMethod(String setMethod) {
/* 105 */     this.setMethod = setMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWriteable()
/*     */   {
/* 113 */     return this.writeable;
/*     */   }
/*     */   
/*     */   public void setWriteable(boolean writeable) {
/* 117 */     this.writeable = writeable;
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
/*     */   MBeanAttributeInfo createAttributeInfo()
/*     */   {
/* 130 */     if (this.info == null)
/*     */     {
/* 132 */       this.info = new MBeanAttributeInfo(getName(), getType(), getDescription(), isReadable(), isWriteable(), false);
/*     */     }
/* 134 */     return (MBeanAttributeInfo)this.info;
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
/*     */   private String getMethodName(String name, boolean getter, boolean is)
/*     */   {
/* 151 */     StringBuilder sb = new StringBuilder();
/* 152 */     if (getter) {
/* 153 */       if (is) {
/* 154 */         sb.append("is");
/*     */       } else
/* 156 */         sb.append("get");
/*     */     } else
/* 158 */       sb.append("set");
/* 159 */     sb.append(Character.toUpperCase(name.charAt(0)));
/* 160 */     sb.append(name.substring(1));
/* 161 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\AttributeInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */