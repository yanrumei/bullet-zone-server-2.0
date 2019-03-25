/*     */ package org.apache.catalina.manager;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Set;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.OperationsException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.mbeans.MBeanDumper;
/*     */ import org.apache.tomcat.util.modeler.Registry;
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
/*     */ public class JMXProxyServlet
/*     */   extends HttpServlet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  52 */   private static final String[] NO_PARAMETERS = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */   protected transient MBeanServer mBeanServer = null;
/*     */   
/*     */ 
/*     */ 
/*     */   protected transient Registry registry;
/*     */   
/*     */ 
/*     */ 
/*     */   public void init()
/*     */     throws ServletException
/*     */   {
/*  69 */     this.registry = Registry.getRegistry(null, null);
/*  70 */     this.mBeanServer = Registry.getRegistry(null, null).getMBeanServer();
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
/*     */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*     */     throws IOException, ServletException
/*     */   {
/*  86 */     response.setContentType("text/plain");
/*     */     
/*  88 */     PrintWriter writer = response.getWriter();
/*     */     
/*  90 */     if (this.mBeanServer == null) {
/*  91 */       writer.println("Error - No mbean server");
/*  92 */       return;
/*     */     }
/*     */     
/*  95 */     String qry = request.getParameter("set");
/*  96 */     if (qry != null) {
/*  97 */       String name = request.getParameter("att");
/*  98 */       String val = request.getParameter("val");
/*     */       
/* 100 */       setAttribute(writer, qry, name, val);
/* 101 */       return;
/*     */     }
/* 103 */     qry = request.getParameter("get");
/* 104 */     if (qry != null) {
/* 105 */       String name = request.getParameter("att");
/* 106 */       getAttribute(writer, qry, name, request.getParameter("key"));
/* 107 */       return;
/*     */     }
/* 109 */     qry = request.getParameter("invoke");
/* 110 */     if (qry != null) {
/* 111 */       String opName = request.getParameter("op");
/* 112 */       String[] params = getInvokeParameters(request.getParameter("ps"));
/* 113 */       invokeOperation(writer, qry, opName, params);
/* 114 */       return;
/*     */     }
/* 116 */     qry = request.getParameter("qry");
/* 117 */     if (qry == null) {
/* 118 */       qry = "*:*";
/*     */     }
/*     */     
/* 121 */     listBeans(writer, qry);
/*     */   }
/*     */   
/*     */   public void getAttribute(PrintWriter writer, String onameStr, String att, String key)
/*     */   {
/*     */     try {
/* 127 */       ObjectName oname = new ObjectName(onameStr);
/* 128 */       Object value = this.mBeanServer.getAttribute(oname, att);
/*     */       
/* 130 */       if ((null != key) && ((value instanceof CompositeData)))
/* 131 */         value = ((CompositeData)value).get(key);
/*     */       String valueStr;
/*     */       String valueStr;
/* 134 */       if (value != null) {
/* 135 */         valueStr = value.toString();
/*     */       } else {
/* 137 */         valueStr = "<null>";
/*     */       }
/*     */       
/* 140 */       writer.print("OK - Attribute get '");
/* 141 */       writer.print(onameStr);
/* 142 */       writer.print("' - ");
/* 143 */       writer.print(att);
/*     */       
/* 145 */       if (null != key) {
/* 146 */         writer.print(" - key '");
/* 147 */         writer.print(key);
/* 148 */         writer.print("'");
/*     */       }
/*     */       
/* 151 */       writer.print(" = ");
/*     */       
/* 153 */       writer.println(MBeanDumper.escape(valueStr));
/*     */     } catch (Exception ex) {
/* 155 */       writer.println("Error - " + ex.toString());
/* 156 */       ex.printStackTrace(writer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAttribute(PrintWriter writer, String onameStr, String att, String val)
/*     */   {
/*     */     try {
/* 163 */       setAttributeInternal(onameStr, att, val);
/* 164 */       writer.println("OK - Attribute set");
/*     */     } catch (Exception ex) {
/* 166 */       writer.println("Error - " + ex.toString());
/* 167 */       ex.printStackTrace(writer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void listBeans(PrintWriter writer, String qry)
/*     */   {
/* 174 */     Set<ObjectName> names = null;
/*     */     try {
/* 176 */       names = this.mBeanServer.queryNames(new ObjectName(qry), null);
/* 177 */       writer.println("OK - Number of results: " + names.size());
/* 178 */       writer.println();
/*     */     } catch (Exception ex) {
/* 180 */       writer.println("Error - " + ex.toString());
/* 181 */       ex.printStackTrace(writer);
/* 182 */       return;
/*     */     }
/*     */     
/* 185 */     String dump = MBeanDumper.dumpBeans(this.mBeanServer, names);
/* 186 */     writer.print(dump);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSupported(String type)
/*     */   {
/* 197 */     return true;
/*     */   }
/*     */   
/*     */   private void invokeOperation(PrintWriter writer, String onameStr, String op, String[] valuesStr)
/*     */   {
/*     */     try
/*     */     {
/* 204 */       Object retVal = invokeOperationInternal(onameStr, op, valuesStr);
/* 205 */       if (retVal != null) {
/* 206 */         writer.println("OK - Operation " + op + " returned:");
/* 207 */         output("", writer, retVal);
/*     */       } else {
/* 209 */         writer.println("OK - Operation " + op + " without return value");
/*     */       }
/*     */     } catch (Exception ex) {
/* 212 */       writer.println("Error - " + ex.toString());
/* 213 */       ex.printStackTrace(writer);
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
/*     */   private String[] getInvokeParameters(String paramString)
/*     */   {
/* 228 */     if (paramString == null) {
/* 229 */       return NO_PARAMETERS;
/*     */     }
/* 231 */     return paramString.split(",");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setAttributeInternal(String onameStr, String attributeName, String value)
/*     */     throws OperationsException, MBeanException, ReflectionException
/*     */   {
/* 240 */     ObjectName oname = new ObjectName(onameStr);
/* 241 */     String type = this.registry.getType(oname, attributeName);
/* 242 */     Object valueObj = this.registry.convertValue(type, value);
/* 243 */     this.mBeanServer.setAttribute(oname, new Attribute(attributeName, valueObj));
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
/*     */   private Object invokeOperationInternal(String onameStr, String operation, String[] parameters)
/*     */     throws OperationsException, MBeanException, ReflectionException
/*     */   {
/* 259 */     ObjectName oname = new ObjectName(onameStr);
/* 260 */     MBeanOperationInfo methodInfo = this.registry.getMethodInfo(oname, operation);
/* 261 */     MBeanParameterInfo[] signature = methodInfo.getSignature();
/* 262 */     String[] signatureTypes = new String[signature.length];
/* 263 */     Object[] values = new Object[signature.length];
/* 264 */     for (int i = 0; i < signature.length; i++) {
/* 265 */       MBeanParameterInfo pi = signature[i];
/* 266 */       signatureTypes[i] = pi.getType();
/* 267 */       values[i] = this.registry.convertValue(pi.getType(), parameters[i]);
/*     */     }
/*     */     
/* 270 */     return this.mBeanServer.invoke(oname, operation, values, signatureTypes);
/*     */   }
/*     */   
/*     */   private void output(String indent, PrintWriter writer, Object result)
/*     */   {
/* 275 */     if ((result instanceof Object[])) {
/* 276 */       for (Object obj : (Object[])result)
/* 277 */         output("  " + indent, writer, obj);
/*     */     } else {
/*     */       String strValue;
/*     */       String strValue;
/* 281 */       if (result != null) {
/* 282 */         strValue = result.toString();
/*     */       } else {
/* 284 */         strValue = "<null>";
/*     */       }
/* 286 */       writer.println(indent + strValue);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\JMXProxyServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */