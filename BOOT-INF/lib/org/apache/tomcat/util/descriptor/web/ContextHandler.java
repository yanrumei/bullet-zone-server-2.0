/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ public class ContextHandler
/*     */   extends ResourceBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  41 */   private String handlerclass = null;
/*     */   
/*     */   public String getHandlerclass() {
/*  44 */     return this.handlerclass;
/*     */   }
/*     */   
/*     */   public void setHandlerclass(String handlerclass) {
/*  48 */     this.handlerclass = handlerclass;
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
/*  60 */   private final HashMap<String, String> soapHeaders = new HashMap();
/*     */   
/*     */   public Iterator<String> getLocalparts() {
/*  63 */     return this.soapHeaders.keySet().iterator();
/*     */   }
/*     */   
/*     */   public String getNamespaceuri(String localpart) {
/*  67 */     return (String)this.soapHeaders.get(localpart);
/*     */   }
/*     */   
/*     */   public void addSoapHeaders(String localpart, String namespaceuri) {
/*  71 */     this.soapHeaders.put(localpart, namespaceuri);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperty(String name, String value)
/*     */   {
/*  80 */     setProperty(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private final ArrayList<String> soapRoles = new ArrayList();
/*     */   
/*     */   public String getSoapRole(int i) {
/*  89 */     return (String)this.soapRoles.get(i);
/*     */   }
/*     */   
/*     */   public int getSoapRolesSize() {
/*  93 */     return this.soapRoles.size();
/*     */   }
/*     */   
/*     */   public void addSoapRole(String soapRole) {
/*  97 */     this.soapRoles.add(soapRole);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   private final ArrayList<String> portNames = new ArrayList();
/*     */   
/*     */   public String getPortName(int i) {
/* 106 */     return (String)this.portNames.get(i);
/*     */   }
/*     */   
/*     */   public int getPortNamesSize() {
/* 110 */     return this.portNames.size();
/*     */   }
/*     */   
/*     */   public void addPortName(String portName) {
/* 114 */     this.portNames.add(portName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 126 */     StringBuilder sb = new StringBuilder("ContextHandler[");
/* 127 */     sb.append("name=");
/* 128 */     sb.append(getName());
/* 129 */     if (this.handlerclass != null) {
/* 130 */       sb.append(", class=");
/* 131 */       sb.append(this.handlerclass);
/*     */     }
/* 133 */     if (this.soapHeaders != null) {
/* 134 */       sb.append(", soap-headers=");
/* 135 */       sb.append(this.soapHeaders);
/*     */     }
/* 137 */     if (getSoapRolesSize() > 0) {
/* 138 */       sb.append(", soap-roles=");
/* 139 */       sb.append(this.soapRoles);
/*     */     }
/* 141 */     if (getPortNamesSize() > 0) {
/* 142 */       sb.append(", port-name=");
/* 143 */       sb.append(this.portNames);
/*     */     }
/* 145 */     if (listProperties() != null) {
/* 146 */       sb.append(", init-param=");
/* 147 */       sb.append(listProperties());
/*     */     }
/* 149 */     sb.append("]");
/* 150 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 156 */     int prime = 31;
/* 157 */     int result = super.hashCode();
/*     */     
/* 159 */     result = 31 * result + (this.handlerclass == null ? 0 : this.handlerclass.hashCode());
/*     */     
/* 161 */     result = 31 * result + (this.portNames == null ? 0 : this.portNames.hashCode());
/*     */     
/* 163 */     result = 31 * result + (this.soapHeaders == null ? 0 : this.soapHeaders.hashCode());
/*     */     
/* 165 */     result = 31 * result + (this.soapRoles == null ? 0 : this.soapRoles.hashCode());
/* 166 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 172 */     if (this == obj) {
/* 173 */       return true;
/*     */     }
/* 175 */     if (!super.equals(obj)) {
/* 176 */       return false;
/*     */     }
/* 178 */     if (getClass() != obj.getClass()) {
/* 179 */       return false;
/*     */     }
/* 181 */     ContextHandler other = (ContextHandler)obj;
/* 182 */     if (this.handlerclass == null) {
/* 183 */       if (other.handlerclass != null) {
/* 184 */         return false;
/*     */       }
/* 186 */     } else if (!this.handlerclass.equals(other.handlerclass)) {
/* 187 */       return false;
/*     */     }
/* 189 */     if (this.portNames == null) {
/* 190 */       if (other.portNames != null) {
/* 191 */         return false;
/*     */       }
/* 193 */     } else if (!this.portNames.equals(other.portNames)) {
/* 194 */       return false;
/*     */     }
/* 196 */     if (this.soapHeaders == null) {
/* 197 */       if (other.soapHeaders != null) {
/* 198 */         return false;
/*     */       }
/* 200 */     } else if (!this.soapHeaders.equals(other.soapHeaders)) {
/* 201 */       return false;
/*     */     }
/* 203 */     if (this.soapRoles == null) {
/* 204 */       if (other.soapRoles != null) {
/* 205 */         return false;
/*     */       }
/* 207 */     } else if (!this.soapRoles.equals(other.soapRoles)) {
/* 208 */       return false;
/*     */     }
/* 210 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */