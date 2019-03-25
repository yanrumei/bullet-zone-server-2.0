/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.Filter;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilterDef
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  39 */   private static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private String description = null;
/*     */   
/*     */   public String getDescription() {
/*  50 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/*  54 */     this.description = description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   private String displayName = null;
/*     */   
/*     */   public String getDisplayName() {
/*  64 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {
/*  68 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   private transient Filter filter = null;
/*     */   
/*     */   public Filter getFilter() {
/*  78 */     return this.filter;
/*     */   }
/*     */   
/*     */   public void setFilter(Filter filter) {
/*  82 */     this.filter = filter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   private String filterClass = null;
/*     */   
/*     */   public String getFilterClass() {
/*  92 */     return this.filterClass;
/*     */   }
/*     */   
/*     */   public void setFilterClass(String filterClass) {
/*  96 */     this.filterClass = filterClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 104 */   private String filterName = null;
/*     */   
/*     */   public String getFilterName() {
/* 107 */     return this.filterName;
/*     */   }
/*     */   
/*     */   public void setFilterName(String filterName) {
/* 111 */     if ((filterName == null) || (filterName.equals("")))
/*     */     {
/* 113 */       throw new IllegalArgumentException(sm.getString("filterDef.invalidFilterName", new Object[] { filterName }));
/*     */     }
/* 115 */     this.filterName = filterName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */   private String largeIcon = null;
/*     */   
/*     */   public String getLargeIcon() {
/* 125 */     return this.largeIcon;
/*     */   }
/*     */   
/*     */   public void setLargeIcon(String largeIcon) {
/* 129 */     this.largeIcon = largeIcon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */   private final Map<String, String> parameters = new HashMap();
/*     */   
/*     */   public Map<String, String> getParameterMap()
/*     */   {
/* 141 */     return this.parameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 149 */   private String smallIcon = null;
/*     */   
/*     */   public String getSmallIcon() {
/* 152 */     return this.smallIcon;
/*     */   }
/*     */   
/*     */   public void setSmallIcon(String smallIcon) {
/* 156 */     this.smallIcon = smallIcon;
/*     */   }
/*     */   
/* 159 */   private String asyncSupported = null;
/*     */   
/*     */   public String getAsyncSupported() {
/* 162 */     return this.asyncSupported;
/*     */   }
/*     */   
/*     */   public void setAsyncSupported(String asyncSupported) {
/* 166 */     this.asyncSupported = asyncSupported;
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
/*     */   public void addInitParameter(String name, String value)
/*     */   {
/* 182 */     if (this.parameters.containsKey(name))
/*     */     {
/*     */ 
/* 185 */       return;
/*     */     }
/* 187 */     this.parameters.put(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 198 */     StringBuilder sb = new StringBuilder("FilterDef[");
/* 199 */     sb.append("filterName=");
/* 200 */     sb.append(this.filterName);
/* 201 */     sb.append(", filterClass=");
/* 202 */     sb.append(this.filterClass);
/* 203 */     sb.append("]");
/* 204 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\FilterDef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */