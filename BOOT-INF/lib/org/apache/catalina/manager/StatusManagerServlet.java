/*     */ package org.apache.catalina.manager;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerNotification;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectInstance;
/*     */ import javax.management.ObjectName;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.util.ServerInfo;
/*     */ import org.apache.tomcat.util.modeler.Registry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StatusManagerServlet
/*     */   extends HttpServlet
/*     */   implements NotificationListener
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  60 */   protected MBeanServer mBeanServer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   protected final Vector<ObjectName> protocolHandlers = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   protected final Vector<ObjectName> threadPools = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   protected final Vector<ObjectName> requestProcessors = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   protected final Vector<ObjectName> globalRequestProcessors = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.manager");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init()
/*     */     throws ServletException
/*     */   {
/* 104 */     this.mBeanServer = Registry.getRegistry(null, null).getMBeanServer();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 109 */       String onStr = "*:type=ProtocolHandler,*";
/* 110 */       ObjectName objectName = new ObjectName(onStr);
/* 111 */       Set<ObjectInstance> set = this.mBeanServer.queryMBeans(objectName, null);
/* 112 */       Iterator<ObjectInstance> iterator = set.iterator();
/* 113 */       while (iterator.hasNext()) {
/* 114 */         ObjectInstance oi = (ObjectInstance)iterator.next();
/* 115 */         this.protocolHandlers.addElement(oi.getObjectName());
/*     */       }
/*     */       
/*     */ 
/* 119 */       onStr = "*:type=ThreadPool,*";
/* 120 */       objectName = new ObjectName(onStr);
/* 121 */       set = this.mBeanServer.queryMBeans(objectName, null);
/* 122 */       iterator = set.iterator();
/* 123 */       while (iterator.hasNext()) {
/* 124 */         ObjectInstance oi = (ObjectInstance)iterator.next();
/* 125 */         this.threadPools.addElement(oi.getObjectName());
/*     */       }
/*     */       
/*     */ 
/* 129 */       onStr = "*:type=GlobalRequestProcessor,*";
/* 130 */       objectName = new ObjectName(onStr);
/* 131 */       set = this.mBeanServer.queryMBeans(objectName, null);
/* 132 */       iterator = set.iterator();
/* 133 */       while (iterator.hasNext()) {
/* 134 */         ObjectInstance oi = (ObjectInstance)iterator.next();
/* 135 */         this.globalRequestProcessors.addElement(oi.getObjectName());
/*     */       }
/*     */       
/*     */ 
/* 139 */       onStr = "*:type=RequestProcessor,*";
/* 140 */       objectName = new ObjectName(onStr);
/* 141 */       set = this.mBeanServer.queryMBeans(objectName, null);
/* 142 */       iterator = set.iterator();
/* 143 */       while (iterator.hasNext()) {
/* 144 */         ObjectInstance oi = (ObjectInstance)iterator.next();
/* 145 */         this.requestProcessors.addElement(oi.getObjectName());
/*     */       }
/*     */       
/*     */ 
/* 149 */       onStr = "JMImplementation:type=MBeanServerDelegate";
/* 150 */       objectName = new ObjectName(onStr);
/* 151 */       this.mBeanServer.addNotificationListener(objectName, this, null, null);
/*     */     }
/*     */     catch (Exception e) {
/* 154 */       e.printStackTrace();
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
/*     */   public void destroy()
/*     */   {
/* 167 */     String onStr = "JMImplementation:type=MBeanServerDelegate";
/*     */     try
/*     */     {
/* 170 */       ObjectName objectName = new ObjectName(onStr);
/* 171 */       this.mBeanServer.removeNotificationListener(objectName, this, null, null);
/*     */     } catch (Exception e) {
/* 173 */       e.printStackTrace();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*     */     throws IOException, ServletException
/*     */   {
/* 194 */     int mode = 0;
/*     */     
/* 196 */     if ((request.getParameter("XML") != null) && 
/* 197 */       (request.getParameter("XML").equals("true"))) {
/* 198 */       mode = 1;
/*     */     }
/* 200 */     StatusTransformer.setContentType(response, mode);
/*     */     
/* 202 */     PrintWriter writer = response.getWriter();
/*     */     
/* 204 */     boolean completeStatus = false;
/* 205 */     if ((request.getPathInfo() != null) && 
/* 206 */       (request.getPathInfo().equals("/all"))) {
/* 207 */       completeStatus = true;
/*     */     }
/*     */     
/* 210 */     Object[] args = new Object[1];
/* 211 */     args[0] = request.getContextPath();
/* 212 */     StatusTransformer.writeHeader(writer, args, mode);
/*     */     
/*     */ 
/* 215 */     args = new Object[2];
/* 216 */     args[0] = request.getContextPath();
/* 217 */     if (completeStatus) {
/* 218 */       args[1] = sm.getString("statusServlet.complete");
/*     */     } else {
/* 220 */       args[1] = sm.getString("statusServlet.title");
/*     */     }
/*     */     
/* 223 */     StatusTransformer.writeBody(writer, args, mode);
/*     */     
/*     */ 
/* 226 */     args = new Object[9];
/* 227 */     args[0] = sm.getString("htmlManagerServlet.manager");
/* 228 */     args[1] = response.encodeURL(request.getContextPath() + "/html/list");
/* 229 */     args[2] = sm.getString("htmlManagerServlet.list");
/* 230 */     args[3] = response
/* 231 */       .encodeURL(request.getContextPath() + "/" + sm
/* 232 */       .getString("htmlManagerServlet.helpHtmlManagerFile"));
/* 233 */     args[4] = sm.getString("htmlManagerServlet.helpHtmlManager");
/* 234 */     args[5] = response
/* 235 */       .encodeURL(request.getContextPath() + "/" + sm
/* 236 */       .getString("htmlManagerServlet.helpManagerFile"));
/* 237 */     args[6] = sm.getString("htmlManagerServlet.helpManager");
/* 238 */     if (completeStatus)
/*     */     {
/* 240 */       args[7] = response.encodeURL(request.getContextPath() + "/status");
/* 241 */       args[8] = sm.getString("statusServlet.title");
/*     */     }
/*     */     else {
/* 244 */       args[7] = response.encodeURL(request.getContextPath() + "/status/all");
/* 245 */       args[8] = sm.getString("statusServlet.complete");
/*     */     }
/*     */     
/* 248 */     StatusTransformer.writeManager(writer, args, mode);
/*     */     
/*     */ 
/* 251 */     args = new Object[9];
/* 252 */     args[0] = sm.getString("htmlManagerServlet.serverTitle");
/* 253 */     args[1] = sm.getString("htmlManagerServlet.serverVersion");
/* 254 */     args[2] = sm.getString("htmlManagerServlet.serverJVMVersion");
/* 255 */     args[3] = sm.getString("htmlManagerServlet.serverJVMVendor");
/* 256 */     args[4] = sm.getString("htmlManagerServlet.serverOSName");
/* 257 */     args[5] = sm.getString("htmlManagerServlet.serverOSVersion");
/* 258 */     args[6] = sm.getString("htmlManagerServlet.serverOSArch");
/* 259 */     args[7] = sm.getString("htmlManagerServlet.serverHostname");
/* 260 */     args[8] = sm.getString("htmlManagerServlet.serverIPAddress");
/*     */     
/* 262 */     StatusTransformer.writePageHeading(writer, args, mode);
/*     */     
/*     */ 
/* 265 */     args = new Object[8];
/* 266 */     args[0] = ServerInfo.getServerInfo();
/* 267 */     args[1] = System.getProperty("java.runtime.version");
/* 268 */     args[2] = System.getProperty("java.vm.vendor");
/* 269 */     args[3] = System.getProperty("os.name");
/* 270 */     args[4] = System.getProperty("os.version");
/* 271 */     args[5] = System.getProperty("os.arch");
/*     */     try {
/* 273 */       InetAddress address = InetAddress.getLocalHost();
/* 274 */       args[6] = address.getHostName();
/* 275 */       args[7] = address.getHostAddress();
/*     */     } catch (UnknownHostException e) {
/* 277 */       args[6] = "-";
/* 278 */       args[7] = "-";
/*     */     }
/*     */     
/* 281 */     StatusTransformer.writeServerInfo(writer, args, mode);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 286 */       StatusTransformer.writeOSState(writer, mode);
/*     */       
/*     */ 
/* 289 */       StatusTransformer.writeVMState(writer, mode);
/*     */       
/* 291 */       Enumeration<ObjectName> enumeration = this.threadPools.elements();
/* 292 */       while (enumeration.hasMoreElements()) {
/* 293 */         ObjectName objectName = (ObjectName)enumeration.nextElement();
/* 294 */         String name = objectName.getKeyProperty("name");
/*     */         
/*     */ 
/* 297 */         StatusTransformer.writeConnectorState(writer, objectName, name, this.mBeanServer, this.globalRequestProcessors, this.requestProcessors, mode);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 302 */       if ((request.getPathInfo() != null) && 
/* 303 */         (request.getPathInfo().equals("/all")))
/*     */       {
/*     */ 
/*     */ 
/* 307 */         StatusTransformer.writeDetailedState(writer, this.mBeanServer, mode);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 311 */       throw new ServletException(e);
/*     */     }
/*     */     
/*     */ 
/* 315 */     StatusTransformer.writeFooter(writer, mode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleNotification(Notification notification, Object handback)
/*     */   {
/* 326 */     if ((notification instanceof MBeanServerNotification))
/*     */     {
/* 328 */       ObjectName objectName = ((MBeanServerNotification)notification).getMBeanName();
/*     */       
/* 330 */       if (notification.getType().equals("JMX.mbean.registered")) {
/* 331 */         String type = objectName.getKeyProperty("type");
/* 332 */         if (type != null) {
/* 333 */           if (type.equals("ProtocolHandler")) {
/* 334 */             this.protocolHandlers.addElement(objectName);
/* 335 */           } else if (type.equals("ThreadPool")) {
/* 336 */             this.threadPools.addElement(objectName);
/* 337 */           } else if (type.equals("GlobalRequestProcessor")) {
/* 338 */             this.globalRequestProcessors.addElement(objectName);
/* 339 */           } else if (type.equals("RequestProcessor")) {
/* 340 */             this.requestProcessors.addElement(objectName);
/*     */           }
/*     */         }
/*     */       }
/* 344 */       else if (notification.getType().equals("JMX.mbean.unregistered")) {
/* 345 */         String type = objectName.getKeyProperty("type");
/* 346 */         if (type != null) {
/* 347 */           if (type.equals("ProtocolHandler")) {
/* 348 */             this.protocolHandlers.removeElement(objectName);
/* 349 */           } else if (type.equals("ThreadPool")) {
/* 350 */             this.threadPools.removeElement(objectName);
/* 351 */           } else if (type.equals("GlobalRequestProcessor")) {
/* 352 */             this.globalRequestProcessors.removeElement(objectName);
/* 353 */           } else if (type.equals("RequestProcessor")) {
/* 354 */             this.requestProcessors.removeElement(objectName);
/*     */           }
/*     */         }
/* 357 */         String j2eeType = objectName.getKeyProperty("j2eeType");
/* 358 */         if (j2eeType == null) {}
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\StatusManagerServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */