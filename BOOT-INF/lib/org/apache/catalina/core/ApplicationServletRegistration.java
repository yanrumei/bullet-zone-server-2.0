/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.MultipartConfigElement;
/*     */ import javax.servlet.ServletRegistration.Dynamic;
/*     */ import javax.servlet.ServletSecurityElement;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.catalina.util.ParameterMap;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
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
/*     */ public class ApplicationServletRegistration
/*     */   implements ServletRegistration.Dynamic
/*     */ {
/*  45 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */   private final Wrapper wrapper;
/*     */   private final Context context;
/*     */   
/*     */   public ApplicationServletRegistration(Wrapper wrapper, Context context)
/*     */   {
/*  52 */     this.wrapper = wrapper;
/*  53 */     this.context = context;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  59 */     return this.wrapper.getServletClass();
/*     */   }
/*     */   
/*     */   public String getInitParameter(String name)
/*     */   {
/*  64 */     return this.wrapper.findInitParameter(name);
/*     */   }
/*     */   
/*     */   public Map<String, String> getInitParameters()
/*     */   {
/*  69 */     ParameterMap<String, String> result = new ParameterMap();
/*     */     
/*  71 */     String[] parameterNames = this.wrapper.findInitParameters();
/*     */     
/*  73 */     for (String parameterName : parameterNames) {
/*  74 */       result.put(parameterName, this.wrapper.findInitParameter(parameterName));
/*     */     }
/*     */     
/*  77 */     result.setLocked(true);
/*  78 */     return result;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  83 */     return this.wrapper.getName();
/*     */   }
/*     */   
/*     */   public boolean setInitParameter(String name, String value)
/*     */   {
/*  88 */     if ((name == null) || (value == null))
/*     */     {
/*  90 */       throw new IllegalArgumentException(sm.getString("applicationFilterRegistration.nullInitParam", new Object[] { name, value }));
/*     */     }
/*     */     
/*  93 */     if (getInitParameter(name) != null) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     this.wrapper.addInitParameter(name, value);
/*     */     
/*  99 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> setInitParameters(Map<String, String> initParameters)
/*     */   {
/* 105 */     Set<String> conflicts = new HashSet();
/*     */     
/* 107 */     for (Map.Entry<String, String> entry : initParameters.entrySet()) {
/* 108 */       if ((entry.getKey() == null) || (entry.getValue() == null)) {
/* 109 */         throw new IllegalArgumentException(sm.getString("applicationFilterRegistration.nullInitParams", new Object[] {entry
/*     */         
/* 111 */           .getKey(), entry.getValue() }));
/*     */       }
/* 113 */       if (getInitParameter((String)entry.getKey()) != null) {
/* 114 */         conflicts.add(entry.getKey());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 120 */     if (conflicts.isEmpty()) {
/* 121 */       for (Map.Entry<String, String> entry : initParameters.entrySet()) {
/* 122 */         setInitParameter((String)entry.getKey(), (String)entry.getValue());
/*     */       }
/*     */     }
/*     */     
/* 126 */     return conflicts;
/*     */   }
/*     */   
/*     */   public void setAsyncSupported(boolean asyncSupported)
/*     */   {
/* 131 */     this.wrapper.setAsyncSupported(asyncSupported);
/*     */   }
/*     */   
/*     */   public void setLoadOnStartup(int loadOnStartup)
/*     */   {
/* 136 */     this.wrapper.setLoadOnStartup(loadOnStartup);
/*     */   }
/*     */   
/*     */   public void setMultipartConfig(MultipartConfigElement multipartConfig)
/*     */   {
/* 141 */     this.wrapper.setMultipartConfigElement(multipartConfig);
/*     */   }
/*     */   
/*     */   public void setRunAsRole(String roleName)
/*     */   {
/* 146 */     this.wrapper.setRunAs(roleName);
/*     */   }
/*     */   
/*     */   public Set<String> setServletSecurity(ServletSecurityElement constraint)
/*     */   {
/* 151 */     if (constraint == null) {
/* 152 */       throw new IllegalArgumentException(sm.getString("applicationServletRegistration.setServletSecurity.iae", new Object[] {
/*     */       
/* 154 */         getName(), this.context.getName() }));
/*     */     }
/*     */     
/* 157 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
/* 158 */       throw new IllegalStateException(sm.getString("applicationServletRegistration.setServletSecurity.ise", new Object[] {
/*     */       
/* 160 */         getName(), this.context.getName() }));
/*     */     }
/*     */     
/* 163 */     return this.context.addServletSecurity(this, constraint);
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> addMapping(String... urlPatterns)
/*     */   {
/* 169 */     if (urlPatterns == null) {
/* 170 */       return Collections.emptySet();
/*     */     }
/*     */     
/* 173 */     Set<String> conflicts = new HashSet();
/*     */     
/* 175 */     for (String urlPattern : urlPatterns) {
/* 176 */       String wrapperName = this.context.findServletMapping(urlPattern);
/* 177 */       if (wrapperName != null) {
/* 178 */         Wrapper wrapper = (Wrapper)this.context.findChild(wrapperName);
/* 179 */         if (wrapper.isOverridable())
/*     */         {
/*     */ 
/* 182 */           this.context.removeServletMapping(urlPattern);
/*     */         } else {
/* 184 */           conflicts.add(urlPattern);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 189 */     if (!conflicts.isEmpty()) {
/* 190 */       return conflicts;
/*     */     }
/*     */     
/* 193 */     for (String urlPattern : urlPatterns) {
/* 194 */       this.context.addServletMappingDecoded(
/* 195 */         UDecoder.URLDecode(urlPattern, StandardCharsets.UTF_8), this.wrapper.getName());
/*     */     }
/* 197 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<String> getMappings()
/*     */   {
/* 203 */     Set<String> result = new HashSet();
/* 204 */     String servletName = this.wrapper.getName();
/*     */     
/* 206 */     String[] urlPatterns = this.context.findServletMappings();
/* 207 */     for (String urlPattern : urlPatterns) {
/* 208 */       String name = this.context.findServletMapping(urlPattern);
/* 209 */       if (name.equals(servletName)) {
/* 210 */         result.add(urlPattern);
/*     */       }
/*     */     }
/* 213 */     return result;
/*     */   }
/*     */   
/*     */   public String getRunAsRole()
/*     */   {
/* 218 */     return this.wrapper.getRunAs();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationServletRegistration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */