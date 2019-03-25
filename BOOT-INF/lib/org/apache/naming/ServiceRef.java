/*     */ package org.apache.naming;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServiceRef
/*     */   extends Reference
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String DEFAULT_FACTORY = "org.apache.naming.factory.webservices.ServiceRefFactory";
/*     */   public static final String SERVICE_INTERFACE = "serviceInterface";
/*     */   public static final String SERVICE_NAMESPACE = "service namespace";
/*     */   public static final String SERVICE_LOCAL_PART = "service local part";
/*     */   public static final String WSDL = "wsdl";
/*     */   public static final String JAXRPCMAPPING = "jaxrpcmapping";
/*     */   public static final String PORTCOMPONENTLINK = "portcomponentlink";
/*     */   public static final String SERVICEENDPOINTINTERFACE = "serviceendpointinterface";
/*  89 */   private final Vector<HandlerRef> handlers = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServiceRef(String refname, String serviceInterface, String[] serviceQname, String wsdl, String jaxrpcmapping)
/*     */   {
/*  96 */     this(refname, serviceInterface, serviceQname, wsdl, jaxrpcmapping, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServiceRef(String refname, String serviceInterface, String[] serviceQname, String wsdl, String jaxrpcmapping, String factory, String factoryLocation)
/*     */   {
/* 104 */     super(serviceInterface, factory, factoryLocation);
/* 105 */     StringRefAddr refAddr = null;
/* 106 */     if (serviceInterface != null) {
/* 107 */       refAddr = new StringRefAddr("serviceInterface", serviceInterface);
/* 108 */       add(refAddr);
/*     */     }
/* 110 */     if (serviceQname[0] != null) {
/* 111 */       refAddr = new StringRefAddr("service namespace", serviceQname[0]);
/* 112 */       add(refAddr);
/*     */     }
/* 114 */     if (serviceQname[1] != null) {
/* 115 */       refAddr = new StringRefAddr("service local part", serviceQname[1]);
/* 116 */       add(refAddr);
/*     */     }
/* 118 */     if (wsdl != null) {
/* 119 */       refAddr = new StringRefAddr("wsdl", wsdl);
/* 120 */       add(refAddr);
/*     */     }
/* 122 */     if (jaxrpcmapping != null) {
/* 123 */       refAddr = new StringRefAddr("jaxrpcmapping", jaxrpcmapping);
/* 124 */       add(refAddr);
/*     */     }
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
/*     */   public HandlerRef getHandler()
/*     */   {
/* 140 */     return (HandlerRef)this.handlers.remove(0);
/*     */   }
/*     */   
/*     */   public int getHandlersSize()
/*     */   {
/* 145 */     return this.handlers.size();
/*     */   }
/*     */   
/*     */   public void addHandler(HandlerRef handler)
/*     */   {
/* 150 */     this.handlers.add(handler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFactoryClassName()
/*     */   {
/* 161 */     String factory = super.getFactoryClassName();
/* 162 */     if (factory != null) {
/* 163 */       return factory;
/*     */     }
/* 165 */     factory = System.getProperty("java.naming.factory.object");
/* 166 */     if (factory != null) {
/* 167 */       return null;
/*     */     }
/* 169 */     return "org.apache.naming.factory.webservices.ServiceRefFactory";
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
/*     */   public String toString()
/*     */   {
/* 184 */     StringBuilder sb = new StringBuilder("ServiceRef[");
/* 185 */     sb.append("className=");
/* 186 */     sb.append(getClassName());
/* 187 */     sb.append(",factoryClassLocation=");
/* 188 */     sb.append(getFactoryClassLocation());
/* 189 */     sb.append(",factoryClassName=");
/* 190 */     sb.append(getFactoryClassName());
/* 191 */     Enumeration<RefAddr> refAddrs = getAll();
/* 192 */     while (refAddrs.hasMoreElements()) {
/* 193 */       RefAddr refAddr = (RefAddr)refAddrs.nextElement();
/* 194 */       sb.append(",{type=");
/* 195 */       sb.append(refAddr.getType());
/* 196 */       sb.append(",content=");
/* 197 */       sb.append(refAddr.getContent());
/* 198 */       sb.append("}");
/*     */     }
/* 200 */     sb.append("]");
/* 201 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\ServiceRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */