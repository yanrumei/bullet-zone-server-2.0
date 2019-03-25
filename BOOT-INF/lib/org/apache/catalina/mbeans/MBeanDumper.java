/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.management.JMRuntimeException;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public class MBeanDumper
/*     */ {
/*  39 */   private static final Log log = LogFactory.getLog(MBeanDumper.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String CRLF = "\r\n";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String dumpBeans(MBeanServer mbeanServer, Set<ObjectName> names)
/*     */   {
/*  51 */     StringBuilder buf = new StringBuilder();
/*  52 */     Iterator<ObjectName> it = names.iterator();
/*  53 */     while (it.hasNext()) {
/*  54 */       ObjectName oname = (ObjectName)it.next();
/*  55 */       buf.append("Name: ");
/*  56 */       buf.append(oname.toString());
/*  57 */       buf.append("\r\n");
/*     */       try
/*     */       {
/*  60 */         MBeanInfo minfo = mbeanServer.getMBeanInfo(oname);
/*     */         
/*  62 */         String code = minfo.getClassName();
/*  63 */         if ("org.apache.commons.modeler.BaseModelMBean".equals(code)) {
/*  64 */           code = (String)mbeanServer.getAttribute(oname, "modelerType");
/*     */         }
/*  66 */         buf.append("modelerType: ");
/*  67 */         buf.append(code);
/*  68 */         buf.append("\r\n");
/*     */         
/*  70 */         MBeanAttributeInfo[] attrs = minfo.getAttributes();
/*  71 */         Object value = null;
/*     */         
/*  73 */         for (int i = 0; i < attrs.length; i++)
/*  74 */           if (attrs[i].isReadable()) {
/*  75 */             String attName = attrs[i].getName();
/*  76 */             if ((!"modelerType".equals(attName)) && 
/*  77 */               (attName.indexOf('=') < 0) && 
/*  78 */               (attName.indexOf(':') < 0) && 
/*  79 */               (attName.indexOf(' ') < 0))
/*     */             {
/*     */ 
/*     */               try
/*     */               {
/*  84 */                 value = mbeanServer.getAttribute(oname, attName);
/*     */               } catch (JMRuntimeException rme) {
/*  86 */                 Throwable cause = rme.getCause();
/*  87 */                 if ((cause instanceof UnsupportedOperationException)) {
/*  88 */                   if (log.isDebugEnabled()) {
/*  89 */                     log.debug("Error getting attribute " + oname + " " + attName, rme);
/*     */                   }
/*     */                 }
/*  92 */                 else if ((cause instanceof NullPointerException)) {
/*  93 */                   if (log.isDebugEnabled()) {
/*  94 */                     log.debug("Error getting attribute " + oname + " " + attName, rme);
/*     */                   }
/*     */                 }
/*     */                 else {
/*  98 */                   log.error("Error getting attribute " + oname + " " + attName, rme);
/*     */                 }
/*     */                 
/* 101 */                 continue;
/*     */               } catch (Throwable t) {
/* 103 */                 ExceptionUtils.handleThrowable(t);
/* 104 */                 log.error("Error getting attribute " + oname + " " + attName, t);
/*     */                 
/* 106 */                 continue;
/*     */               }
/* 108 */               if (value != null)
/*     */                 try
/*     */                 {
/* 111 */                   Class<?> c = value.getClass();
/* 112 */                   String valueString; String valueString; if (c.isArray()) {
/* 113 */                     int len = Array.getLength(value);
/*     */                     
/* 115 */                     StringBuilder sb = new StringBuilder("Array[" + c.getComponentType().getName() + "] of length " + len);
/* 116 */                     if (len > 0) {
/* 117 */                       sb.append("\r\n");
/*     */                     }
/* 119 */                     for (int j = 0; j < len; j++) {
/* 120 */                       sb.append("\t");
/* 121 */                       Object item = Array.get(value, j);
/* 122 */                       if (item == null) {
/* 123 */                         sb.append("NULL VALUE");
/*     */                       } else {
/*     */                         try {
/* 126 */                           sb.append(escape(item.toString()));
/*     */                         }
/*     */                         catch (Throwable t) {
/* 129 */                           ExceptionUtils.handleThrowable(t);
/* 130 */                           sb.append("NON-STRINGABLE VALUE");
/*     */                         }
/*     */                       }
/* 133 */                       if (j < len - 1) {
/* 134 */                         sb.append("\r\n");
/*     */                       }
/*     */                     }
/* 137 */                     valueString = sb.toString();
/*     */                   }
/*     */                   else {
/* 140 */                     valueString = escape(value.toString());
/*     */                   }
/* 142 */                   buf.append(attName);
/* 143 */                   buf.append(": ");
/* 144 */                   buf.append(valueString);
/* 145 */                   buf.append("\r\n");
/*     */                 }
/*     */                 catch (Throwable t) {
/* 148 */                   ExceptionUtils.handleThrowable(t);
/*     */                 }
/*     */             }
/*     */           }
/* 152 */       } catch (Throwable t) { ExceptionUtils.handleThrowable(t);
/*     */       }
/* 154 */       buf.append("\r\n");
/*     */     }
/* 156 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escape(String value)
/*     */   {
/* 164 */     int idx = value.indexOf("\n");
/* 165 */     if (idx < 0) { return value;
/*     */     }
/* 167 */     int prev = 0;
/* 168 */     StringBuilder sb = new StringBuilder();
/* 169 */     while (idx >= 0) {
/* 170 */       appendHead(sb, value, prev, idx);
/*     */       
/* 172 */       sb.append("\\n\n ");
/* 173 */       prev = idx + 1;
/* 174 */       if (idx == value.length() - 1) break;
/* 175 */       idx = value.indexOf('\n', idx + 1);
/*     */     }
/* 177 */     if (prev < value.length())
/* 178 */       appendHead(sb, value, prev, value.length());
/* 179 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static void appendHead(StringBuilder sb, String value, int start, int end) {
/* 183 */     if (end < 1) { return;
/*     */     }
/* 185 */     int pos = start;
/* 186 */     while (end - pos > 78) {
/* 187 */       sb.append(value.substring(pos, pos + 78));
/* 188 */       sb.append("\n ");
/* 189 */       pos += 78;
/*     */     }
/* 191 */     sb.append(value.substring(pos, end));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\MBeanDumper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */