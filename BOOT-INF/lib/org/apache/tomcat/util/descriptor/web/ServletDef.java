/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class ServletDef
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   private static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */   private String description = null;
/*     */   
/*     */   public String getDescription() {
/*  49 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/*  53 */     this.description = description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   private String displayName = null;
/*     */   
/*     */   public String getDisplayName() {
/*  63 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {
/*  67 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private String smallIcon = null;
/*     */   
/*     */   public String getSmallIcon() {
/*  77 */     return this.smallIcon;
/*     */   }
/*     */   
/*     */   public void setSmallIcon(String smallIcon) {
/*  81 */     this.smallIcon = smallIcon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  87 */   private String largeIcon = null;
/*     */   
/*     */   public String getLargeIcon() {
/*  90 */     return this.largeIcon;
/*     */   }
/*     */   
/*     */   public void setLargeIcon(String largeIcon) {
/*  94 */     this.largeIcon = largeIcon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private String servletName = null;
/*     */   
/*     */   public String getServletName() {
/* 105 */     return this.servletName;
/*     */   }
/*     */   
/*     */   public void setServletName(String servletName) {
/* 109 */     if ((servletName == null) || (servletName.equals("")))
/*     */     {
/* 111 */       throw new IllegalArgumentException(sm.getString("servletDef.invalidServletName", new Object[] { servletName }));
/*     */     }
/* 113 */     this.servletName = servletName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */   private String servletClass = null;
/*     */   
/*     */   public String getServletClass() {
/* 123 */     return this.servletClass;
/*     */   }
/*     */   
/*     */   public void setServletClass(String servletClass) {
/* 127 */     this.servletClass = servletClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */   private String jspFile = null;
/*     */   
/*     */   public String getJspFile() {
/* 137 */     return this.jspFile;
/*     */   }
/*     */   
/*     */   public void setJspFile(String jspFile) {
/* 141 */     this.jspFile = jspFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 149 */   private final Map<String, String> parameters = new HashMap();
/*     */   
/*     */   public Map<String, String> getParameterMap()
/*     */   {
/* 153 */     return this.parameters;
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
/*     */   public void addInitParameter(String name, String value)
/*     */   {
/* 166 */     if (this.parameters.containsKey(name))
/*     */     {
/*     */ 
/* 169 */       return;
/*     */     }
/* 171 */     this.parameters.put(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */   private Integer loadOnStartup = null;
/*     */   
/*     */   public Integer getLoadOnStartup() {
/* 181 */     return this.loadOnStartup;
/*     */   }
/*     */   
/*     */   public void setLoadOnStartup(String loadOnStartup) {
/* 185 */     this.loadOnStartup = Integer.valueOf(loadOnStartup);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */   private String runAs = null;
/*     */   
/*     */   public String getRunAs() {
/* 195 */     return this.runAs;
/*     */   }
/*     */   
/*     */   public void setRunAs(String runAs) {
/* 199 */     this.runAs = runAs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */   private final Set<SecurityRoleRef> securityRoleRefs = new HashSet();
/*     */   
/*     */   public Set<SecurityRoleRef> getSecurityRoleRefs() {
/* 209 */     return this.securityRoleRefs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSecurityRoleRef(SecurityRoleRef securityRoleRef)
/*     */   {
/* 218 */     this.securityRoleRefs.add(securityRoleRef);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 224 */   private MultipartDef multipartDef = null;
/*     */   
/*     */   public MultipartDef getMultipartDef() {
/* 227 */     return this.multipartDef;
/*     */   }
/*     */   
/*     */   public void setMultipartDef(MultipartDef multipartDef) {
/* 231 */     this.multipartDef = multipartDef;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */   private Boolean asyncSupported = null;
/*     */   
/*     */   public Boolean getAsyncSupported() {
/* 241 */     return this.asyncSupported;
/*     */   }
/*     */   
/*     */   public void setAsyncSupported(String asyncSupported) {
/* 245 */     this.asyncSupported = Boolean.valueOf(asyncSupported);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 252 */   private Boolean enabled = null;
/*     */   
/*     */   public Boolean getEnabled() {
/* 255 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(String enabled) {
/* 259 */     this.enabled = Boolean.valueOf(enabled);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 266 */   private boolean overridable = false;
/*     */   
/*     */   public boolean isOverridable() {
/* 269 */     return this.overridable;
/*     */   }
/*     */   
/*     */   public void setOverridable(boolean overridable) {
/* 273 */     this.overridable = overridable;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ServletDef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */