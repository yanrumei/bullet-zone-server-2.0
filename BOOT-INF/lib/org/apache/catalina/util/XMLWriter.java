/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class XMLWriter
/*     */ {
/*     */   public static final int OPENING = 0;
/*     */   public static final int CLOSING = 1;
/*     */   public static final int NO_CONTENT = 2;
/*  56 */   protected StringBuilder buffer = new StringBuilder();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Writer writer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XMLWriter()
/*     */   {
/*  72 */     this(null);
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
/*     */   public XMLWriter(Writer writer)
/*     */   {
/*  85 */     this.writer = writer;
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
/*     */   public String toString()
/*     */   {
/*  99 */     return this.buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeProperty(String namespace, String name, String value)
/*     */   {
/* 111 */     writeElement(namespace, name, 0);
/* 112 */     this.buffer.append(value);
/* 113 */     writeElement(namespace, name, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeElement(String namespace, String name, int type)
/*     */   {
/* 125 */     writeElement(namespace, null, name, type);
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
/*     */   public void writeElement(String namespace, String namespaceInfo, String name, int type)
/*     */   {
/* 139 */     if ((namespace != null) && (namespace.length() > 0)) {}
/* 140 */     switch (type) {
/*     */     case 0: 
/* 142 */       if (namespaceInfo != null) {
/* 143 */         this.buffer.append("<" + namespace + ":" + name + " xmlns:" + namespace + "=\"" + namespaceInfo + "\">");
/*     */       }
/*     */       else
/*     */       {
/* 147 */         this.buffer.append("<" + namespace + ":" + name + ">");
/*     */       }
/* 149 */       break;
/*     */     case 1: 
/* 151 */       this.buffer.append("</" + namespace + ":" + name + ">\n");
/* 152 */       break;
/*     */     case 2: 
/*     */     default: 
/* 155 */       if (namespaceInfo != null) {
/* 156 */         this.buffer.append("<" + namespace + ":" + name + " xmlns:" + namespace + "=\"" + namespaceInfo + "\"/>");
/*     */       }
/*     */       else
/*     */       {
/* 160 */         this.buffer.append("<" + namespace + ":" + name + "/>");
/*     */         
/* 162 */         break;
/*     */         
/*     */ 
/* 165 */         switch (type) {
/*     */         case 0: 
/* 167 */           this.buffer.append("<" + name + ">");
/* 168 */           break;
/*     */         case 1: 
/* 170 */           this.buffer.append("</" + name + ">\n");
/* 171 */           break;
/*     */         case 2: 
/*     */         default: 
/* 174 */           this.buffer.append("<" + name + "/>");
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeText(String text)
/*     */   {
/* 187 */     this.buffer.append(text);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeData(String data)
/*     */   {
/* 197 */     this.buffer.append("<![CDATA[" + data + "]]>");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeXMLHeader()
/*     */   {
/* 205 */     this.buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendData()
/*     */     throws IOException
/*     */   {
/* 215 */     if (this.writer != null) {
/* 216 */       this.writer.write(this.buffer.toString());
/* 217 */       this.buffer = new StringBuilder();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\XMLWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */