/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.FilterRegistration.Dynamic;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.util.ParameterMap;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterDef;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterMap;
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
/*     */ public class ApplicationFilterRegistration
/*     */   implements FilterRegistration.Dynamic
/*     */ {
/*  42 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */   private final FilterDef filterDef;
/*     */   private final Context context;
/*     */   
/*     */   public ApplicationFilterRegistration(FilterDef filterDef, Context context)
/*     */   {
/*  49 */     this.filterDef = filterDef;
/*  50 */     this.context = context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames)
/*     */   {
/*  59 */     FilterMap filterMap = new FilterMap();
/*     */     
/*  61 */     filterMap.setFilterName(this.filterDef.getFilterName());
/*     */     Object localObject;
/*  63 */     if (dispatcherTypes != null) {
/*  64 */       for (localObject = dispatcherTypes.iterator(); ((Iterator)localObject).hasNext();) { dispatcherType = (DispatcherType)((Iterator)localObject).next();
/*  65 */         filterMap.setDispatcher(dispatcherType.name());
/*     */       }
/*     */     }
/*     */     DispatcherType dispatcherType;
/*  69 */     if (servletNames != null) {
/*  70 */       localObject = servletNames;dispatcherType = localObject.length; for (DispatcherType localDispatcherType1 = 0; localDispatcherType1 < dispatcherType; localDispatcherType1++) { String servletName = localObject[localDispatcherType1];
/*  71 */         filterMap.addServletName(servletName);
/*     */       }
/*     */       
/*  74 */       if (isMatchAfter) {
/*  75 */         this.context.addFilterMap(filterMap);
/*     */       } else {
/*  77 */         this.context.addFilterMapBefore(filterMap);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMappingForUrlPatterns(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... urlPatterns)
/*     */   {
/*  88 */     FilterMap filterMap = new FilterMap();
/*     */     
/*  90 */     filterMap.setFilterName(this.filterDef.getFilterName());
/*     */     Object localObject;
/*  92 */     if (dispatcherTypes != null) {
/*  93 */       for (localObject = dispatcherTypes.iterator(); ((Iterator)localObject).hasNext();) { dispatcherType = (DispatcherType)((Iterator)localObject).next();
/*  94 */         filterMap.setDispatcher(dispatcherType.name());
/*     */       }
/*     */     }
/*     */     DispatcherType dispatcherType;
/*  98 */     if (urlPatterns != null)
/*     */     {
/* 100 */       localObject = urlPatterns;dispatcherType = localObject.length; for (DispatcherType localDispatcherType1 = 0; localDispatcherType1 < dispatcherType; localDispatcherType1++) { String urlPattern = localObject[localDispatcherType1];
/* 101 */         filterMap.addURLPattern(urlPattern);
/*     */       }
/*     */       
/* 104 */       if (isMatchAfter) {
/* 105 */         this.context.addFilterMap(filterMap);
/*     */       } else {
/* 107 */         this.context.addFilterMapBefore(filterMap);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<String> getServletNameMappings()
/*     */   {
/* 116 */     Collection<String> result = new HashSet();
/*     */     
/* 118 */     FilterMap[] filterMaps = this.context.findFilterMaps();
/*     */     
/* 120 */     for (FilterMap filterMap : filterMaps) {
/* 121 */       if (filterMap.getFilterName().equals(this.filterDef.getFilterName())) {
/* 122 */         for (String servletName : filterMap.getServletNames()) {
/* 123 */           result.add(servletName);
/*     */         }
/*     */       }
/*     */     }
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   public Collection<String> getUrlPatternMappings()
/*     */   {
/* 132 */     Collection<String> result = new HashSet();
/*     */     
/* 134 */     FilterMap[] filterMaps = this.context.findFilterMaps();
/*     */     
/* 136 */     for (FilterMap filterMap : filterMaps) {
/* 137 */       if (filterMap.getFilterName().equals(this.filterDef.getFilterName())) {
/* 138 */         for (String urlPattern : filterMap.getURLPatterns()) {
/* 139 */           result.add(urlPattern);
/*     */         }
/*     */       }
/*     */     }
/* 143 */     return result;
/*     */   }
/*     */   
/*     */   public String getClassName()
/*     */   {
/* 148 */     return this.filterDef.getFilterClass();
/*     */   }
/*     */   
/*     */   public String getInitParameter(String name)
/*     */   {
/* 153 */     return (String)this.filterDef.getParameterMap().get(name);
/*     */   }
/*     */   
/*     */   public Map<String, String> getInitParameters()
/*     */   {
/* 158 */     ParameterMap<String, String> result = new ParameterMap();
/* 159 */     result.putAll(this.filterDef.getParameterMap());
/* 160 */     result.setLocked(true);
/* 161 */     return result;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 166 */     return this.filterDef.getFilterName();
/*     */   }
/*     */   
/*     */   public boolean setInitParameter(String name, String value)
/*     */   {
/* 171 */     if ((name == null) || (value == null))
/*     */     {
/* 173 */       throw new IllegalArgumentException(sm.getString("applicationFilterRegistration.nullInitParam", new Object[] { name, value }));
/*     */     }
/*     */     
/* 176 */     if (getInitParameter(name) != null) {
/* 177 */       return false;
/*     */     }
/*     */     
/* 180 */     this.filterDef.addInitParameter(name, value);
/*     */     
/* 182 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> setInitParameters(Map<String, String> initParameters)
/*     */   {
/* 188 */     Set<String> conflicts = new HashSet();
/*     */     
/* 190 */     for (Map.Entry<String, String> entry : initParameters.entrySet()) {
/* 191 */       if ((entry.getKey() == null) || (entry.getValue() == null)) {
/* 192 */         throw new IllegalArgumentException(sm.getString("applicationFilterRegistration.nullInitParams", new Object[] {entry
/*     */         
/* 194 */           .getKey(), entry.getValue() }));
/*     */       }
/* 196 */       if (getInitParameter((String)entry.getKey()) != null) {
/* 197 */         conflicts.add(entry.getKey());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 203 */     for (Map.Entry<String, String> entry : initParameters.entrySet()) {
/* 204 */       setInitParameter((String)entry.getKey(), (String)entry.getValue());
/*     */     }
/*     */     
/* 207 */     return conflicts;
/*     */   }
/*     */   
/*     */   public void setAsyncSupported(boolean asyncSupported)
/*     */   {
/* 212 */     this.filterDef.setAsyncSupported(Boolean.valueOf(asyncSupported).toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationFilterRegistration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */