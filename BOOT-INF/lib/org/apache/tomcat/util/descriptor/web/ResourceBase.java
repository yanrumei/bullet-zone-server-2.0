/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class ResourceBase
/*     */   implements Serializable, Injectable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  42 */   private String description = null;
/*     */   
/*     */   public String getDescription() {
/*  45 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/*  49 */     this.description = description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private String name = null;
/*     */   
/*     */   public String getName()
/*     */   {
/*  61 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  65 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   private String type = null;
/*     */   
/*     */   public String getType() {
/*  75 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  79 */     this.type = type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private final HashMap<String, Object> properties = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getProperty(String name)
/*     */   {
/*  93 */     return this.properties.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */   {
/* 102 */     this.properties.put(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeProperty(String name)
/*     */   {
/* 110 */     this.properties.remove(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<String> listProperties()
/*     */   {
/* 118 */     return this.properties.keySet().iterator();
/*     */   }
/*     */   
/* 121 */   private final List<InjectionTarget> injectionTargets = new ArrayList();
/*     */   
/*     */   public void addInjectionTarget(String injectionTargetName, String jndiName)
/*     */   {
/* 125 */     InjectionTarget target = new InjectionTarget(injectionTargetName, jndiName);
/* 126 */     this.injectionTargets.add(target);
/*     */   }
/*     */   
/*     */   public List<InjectionTarget> getInjectionTargets()
/*     */   {
/* 131 */     return this.injectionTargets;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 137 */     int prime = 31;
/* 138 */     int result = 1;
/*     */     
/* 140 */     result = 31 * result + (this.description == null ? 0 : this.description.hashCode());
/*     */     
/* 142 */     result = 31 * result + (this.injectionTargets == null ? 0 : this.injectionTargets.hashCode());
/* 143 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/*     */     
/* 145 */     result = 31 * result + (this.properties == null ? 0 : this.properties.hashCode());
/* 146 */     result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
/* 147 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 153 */     if (this == obj) {
/* 154 */       return true;
/*     */     }
/* 156 */     if (obj == null) {
/* 157 */       return false;
/*     */     }
/* 159 */     if (getClass() != obj.getClass()) {
/* 160 */       return false;
/*     */     }
/* 162 */     ResourceBase other = (ResourceBase)obj;
/* 163 */     if (this.description == null) {
/* 164 */       if (other.description != null) {
/* 165 */         return false;
/*     */       }
/* 167 */     } else if (!this.description.equals(other.description)) {
/* 168 */       return false;
/*     */     }
/* 170 */     if (this.injectionTargets == null) {
/* 171 */       if (other.injectionTargets != null) {
/* 172 */         return false;
/*     */       }
/* 174 */     } else if (!this.injectionTargets.equals(other.injectionTargets)) {
/* 175 */       return false;
/*     */     }
/* 177 */     if (this.name == null) {
/* 178 */       if (other.name != null) {
/* 179 */         return false;
/*     */       }
/* 181 */     } else if (!this.name.equals(other.name)) {
/* 182 */       return false;
/*     */     }
/* 184 */     if (this.properties == null) {
/* 185 */       if (other.properties != null) {
/* 186 */         return false;
/*     */       }
/* 188 */     } else if (!this.properties.equals(other.properties)) {
/* 189 */       return false;
/*     */     }
/* 191 */     if (this.type == null) {
/* 192 */       if (other.type != null) {
/* 193 */         return false;
/*     */       }
/* 195 */     } else if (!this.type.equals(other.type)) {
/* 196 */       return false;
/*     */     }
/* 198 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 207 */   private NamingResources resources = null;
/*     */   
/*     */   public NamingResources getNamingResources() {
/* 210 */     return this.resources;
/*     */   }
/*     */   
/*     */   public void setNamingResources(NamingResources resources) {
/* 214 */     this.resources = resources;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ResourceBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */