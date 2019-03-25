/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ public class ContextService
/*     */   extends ResourceBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  41 */   private String displayname = null;
/*     */   
/*     */   public String getDisplayname() {
/*  44 */     return this.displayname;
/*     */   }
/*     */   
/*     */   public void setDisplayname(String displayname) {
/*  48 */     this.displayname = displayname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private String largeIcon = null;
/*     */   
/*     */   public String getLargeIcon() {
/*  57 */     return this.largeIcon;
/*     */   }
/*     */   
/*     */   public void setLargeIcon(String largeIcon) {
/*  61 */     this.largeIcon = largeIcon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private String smallIcon = null;
/*     */   
/*     */   public String getSmallIcon() {
/*  70 */     return this.smallIcon;
/*     */   }
/*     */   
/*     */   public void setSmallIcon(String smallIcon) {
/*  74 */     this.smallIcon = smallIcon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */   private String serviceInterface = null;
/*     */   
/*     */   public String getInterface() {
/*  84 */     return this.serviceInterface;
/*     */   }
/*     */   
/*     */   public void setInterface(String serviceInterface) {
/*  88 */     this.serviceInterface = serviceInterface;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */   private String wsdlfile = null;
/*     */   
/*     */   public String getWsdlfile() {
/*  98 */     return this.wsdlfile;
/*     */   }
/*     */   
/*     */   public void setWsdlfile(String wsdlfile) {
/* 102 */     this.wsdlfile = wsdlfile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   private String jaxrpcmappingfile = null;
/*     */   
/*     */   public String getJaxrpcmappingfile() {
/* 112 */     return this.jaxrpcmappingfile;
/*     */   }
/*     */   
/*     */   public void setJaxrpcmappingfile(String jaxrpcmappingfile) {
/* 116 */     this.jaxrpcmappingfile = jaxrpcmappingfile;
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
/* 130 */   private String[] serviceqname = new String[2];
/*     */   
/*     */   public String[] getServiceqname() {
/* 133 */     return this.serviceqname;
/*     */   }
/*     */   
/*     */   public String getServiceqname(int i) {
/* 137 */     return this.serviceqname[i];
/*     */   }
/*     */   
/*     */   public String getServiceqnameNamespaceURI() {
/* 141 */     return this.serviceqname[0];
/*     */   }
/*     */   
/*     */   public String getServiceqnameLocalpart() {
/* 145 */     return this.serviceqname[1];
/*     */   }
/*     */   
/*     */   public void setServiceqname(String[] serviceqname) {
/* 149 */     this.serviceqname = serviceqname;
/*     */   }
/*     */   
/*     */   public void setServiceqname(String serviceqname, int i) {
/* 153 */     this.serviceqname[i] = serviceqname;
/*     */   }
/*     */   
/*     */   public void setServiceqnameNamespaceURI(String namespaceuri) {
/* 157 */     this.serviceqname[0] = namespaceuri;
/*     */   }
/*     */   
/*     */   public void setServiceqnameLocalpart(String localpart) {
/* 161 */     this.serviceqname[1] = localpart;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<String> getServiceendpoints()
/*     */   {
/* 171 */     return listProperties();
/*     */   }
/*     */   
/*     */   public String getPortlink(String serviceendpoint) {
/* 175 */     return (String)getProperty(serviceendpoint);
/*     */   }
/*     */   
/*     */   public void addPortcomponent(String serviceendpoint, String portlink) {
/* 179 */     if (portlink == null)
/* 180 */       portlink = "";
/* 181 */     setProperty(serviceendpoint, portlink);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 189 */   private final HashMap<String, ContextHandler> handlers = new HashMap();
/*     */   
/*     */   public Iterator<String> getHandlers() {
/* 192 */     return this.handlers.keySet().iterator();
/*     */   }
/*     */   
/*     */   public ContextHandler getHandler(String handlername) {
/* 196 */     return (ContextHandler)this.handlers.get(handlername);
/*     */   }
/*     */   
/*     */   public void addHandler(ContextHandler handler) {
/* 200 */     this.handlers.put(handler.getName(), handler);
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
/*     */   public String toString()
/*     */   {
/* 213 */     StringBuilder sb = new StringBuilder("ContextService[");
/* 214 */     sb.append("name=");
/* 215 */     sb.append(getName());
/* 216 */     if (getDescription() != null) {
/* 217 */       sb.append(", description=");
/* 218 */       sb.append(getDescription());
/*     */     }
/* 220 */     if (getType() != null) {
/* 221 */       sb.append(", type=");
/* 222 */       sb.append(getType());
/*     */     }
/* 224 */     if (this.displayname != null) {
/* 225 */       sb.append(", displayname=");
/* 226 */       sb.append(this.displayname);
/*     */     }
/* 228 */     if (this.largeIcon != null) {
/* 229 */       sb.append(", largeIcon=");
/* 230 */       sb.append(this.largeIcon);
/*     */     }
/* 232 */     if (this.smallIcon != null) {
/* 233 */       sb.append(", smallIcon=");
/* 234 */       sb.append(this.smallIcon);
/*     */     }
/* 236 */     if (this.wsdlfile != null) {
/* 237 */       sb.append(", wsdl-file=");
/* 238 */       sb.append(this.wsdlfile);
/*     */     }
/* 240 */     if (this.jaxrpcmappingfile != null) {
/* 241 */       sb.append(", jaxrpc-mapping-file=");
/* 242 */       sb.append(this.jaxrpcmappingfile);
/*     */     }
/* 244 */     if (this.serviceqname[0] != null) {
/* 245 */       sb.append(", service-qname/namespaceURI=");
/* 246 */       sb.append(this.serviceqname[0]);
/*     */     }
/* 248 */     if (this.serviceqname[1] != null) {
/* 249 */       sb.append(", service-qname/localpart=");
/* 250 */       sb.append(this.serviceqname[1]);
/*     */     }
/* 252 */     if (getServiceendpoints() != null) {
/* 253 */       sb.append(", port-component/service-endpoint-interface=");
/* 254 */       sb.append(getServiceendpoints());
/*     */     }
/* 256 */     if (this.handlers != null) {
/* 257 */       sb.append(", handler=");
/* 258 */       sb.append(this.handlers);
/*     */     }
/* 260 */     sb.append("]");
/* 261 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 267 */     int prime = 31;
/* 268 */     int result = super.hashCode();
/*     */     
/* 270 */     result = 31 * result + (this.displayname == null ? 0 : this.displayname.hashCode());
/*     */     
/* 272 */     result = 31 * result + (this.handlers == null ? 0 : this.handlers.hashCode());
/*     */     
/*     */ 
/* 275 */     result = 31 * result + (this.jaxrpcmappingfile == null ? 0 : this.jaxrpcmappingfile.hashCode());
/*     */     
/* 277 */     result = 31 * result + (this.largeIcon == null ? 0 : this.largeIcon.hashCode());
/*     */     
/* 279 */     result = 31 * result + (this.serviceInterface == null ? 0 : this.serviceInterface.hashCode());
/* 280 */     result = 31 * result + Arrays.hashCode(this.serviceqname);
/*     */     
/* 282 */     result = 31 * result + (this.smallIcon == null ? 0 : this.smallIcon.hashCode());
/*     */     
/* 284 */     result = 31 * result + (this.wsdlfile == null ? 0 : this.wsdlfile.hashCode());
/* 285 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 291 */     if (this == obj) {
/* 292 */       return true;
/*     */     }
/* 294 */     if (!super.equals(obj)) {
/* 295 */       return false;
/*     */     }
/* 297 */     if (getClass() != obj.getClass()) {
/* 298 */       return false;
/*     */     }
/* 300 */     ContextService other = (ContextService)obj;
/* 301 */     if (this.displayname == null) {
/* 302 */       if (other.displayname != null) {
/* 303 */         return false;
/*     */       }
/* 305 */     } else if (!this.displayname.equals(other.displayname)) {
/* 306 */       return false;
/*     */     }
/* 308 */     if (this.handlers == null) {
/* 309 */       if (other.handlers != null) {
/* 310 */         return false;
/*     */       }
/* 312 */     } else if (!this.handlers.equals(other.handlers)) {
/* 313 */       return false;
/*     */     }
/* 315 */     if (this.jaxrpcmappingfile == null) {
/* 316 */       if (other.jaxrpcmappingfile != null) {
/* 317 */         return false;
/*     */       }
/* 319 */     } else if (!this.jaxrpcmappingfile.equals(other.jaxrpcmappingfile)) {
/* 320 */       return false;
/*     */     }
/* 322 */     if (this.largeIcon == null) {
/* 323 */       if (other.largeIcon != null) {
/* 324 */         return false;
/*     */       }
/* 326 */     } else if (!this.largeIcon.equals(other.largeIcon)) {
/* 327 */       return false;
/*     */     }
/* 329 */     if (this.serviceInterface == null) {
/* 330 */       if (other.serviceInterface != null) {
/* 331 */         return false;
/*     */       }
/* 333 */     } else if (!this.serviceInterface.equals(other.serviceInterface)) {
/* 334 */       return false;
/*     */     }
/* 336 */     if (!Arrays.equals(this.serviceqname, other.serviceqname)) {
/* 337 */       return false;
/*     */     }
/* 339 */     if (this.smallIcon == null) {
/* 340 */       if (other.smallIcon != null) {
/* 341 */         return false;
/*     */       }
/* 343 */     } else if (!this.smallIcon.equals(other.smallIcon)) {
/* 344 */       return false;
/*     */     }
/* 346 */     if (this.wsdlfile == null) {
/* 347 */       if (other.wsdlfile != null) {
/* 348 */         return false;
/*     */       }
/* 350 */     } else if (!this.wsdlfile.equals(other.wsdlfile)) {
/* 351 */       return false;
/*     */     }
/* 353 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */