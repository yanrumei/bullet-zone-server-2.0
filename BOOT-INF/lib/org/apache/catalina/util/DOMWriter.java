/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DOMWriter
/*     */ {
/*     */   private static final String PRINTWRITER_ENCODING = "UTF8";
/*     */   @Deprecated
/*     */   protected final PrintWriter out;
/*     */   @Deprecated
/*     */   protected final boolean canonical;
/*     */   
/*     */   public DOMWriter(Writer writer)
/*     */   {
/*  53 */     this(writer, true);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public DOMWriter(Writer writer, boolean canonical)
/*     */   {
/*  59 */     this.out = new PrintWriter(writer);
/*  60 */     this.canonical = canonical;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static String getWriterEncoding()
/*     */   {
/*  71 */     return "UTF8";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(Node node)
/*     */   {
/*  82 */     if (node == null) {
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     int type = node.getNodeType();
/*  87 */     switch (type)
/*     */     {
/*     */     case 9: 
/*  90 */       if (!this.canonical) {
/*  91 */         this.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
/*     */       }
/*  93 */       print(((Document)node).getDocumentElement());
/*  94 */       this.out.flush();
/*  95 */       break;
/*     */     
/*     */ 
/*     */     case 1: 
/*  99 */       this.out.print('<');
/* 100 */       this.out.print(node.getLocalName());
/* 101 */       Attr[] attrs = sortAttributes(node.getAttributes());
/* 102 */       for (int i = 0; i < attrs.length; i++) {
/* 103 */         Attr attr = attrs[i];
/* 104 */         this.out.print(' ');
/* 105 */         this.out.print(attr.getLocalName());
/*     */         
/* 107 */         this.out.print("=\"");
/* 108 */         this.out.print(normalize(attr.getNodeValue()));
/* 109 */         this.out.print('"');
/*     */       }
/* 111 */       this.out.print('>');
/* 112 */       printChildren(node);
/* 113 */       break;
/*     */     
/*     */ 
/*     */     case 5: 
/* 117 */       if (this.canonical) {
/* 118 */         printChildren(node);
/*     */       } else {
/* 120 */         this.out.print('&');
/* 121 */         this.out.print(node.getLocalName());
/* 122 */         this.out.print(';');
/*     */       }
/* 124 */       break;
/*     */     
/*     */ 
/*     */     case 4: 
/* 128 */       if (this.canonical) {
/* 129 */         this.out.print(normalize(node.getNodeValue()));
/*     */       } else {
/* 131 */         this.out.print("<![CDATA[");
/* 132 */         this.out.print(node.getNodeValue());
/* 133 */         this.out.print("]]>");
/*     */       }
/* 135 */       break;
/*     */     
/*     */ 
/*     */     case 3: 
/* 139 */       this.out.print(normalize(node.getNodeValue()));
/* 140 */       break;
/*     */     
/*     */ 
/*     */     case 7: 
/* 144 */       this.out.print("<?");
/* 145 */       this.out.print(node.getLocalName());
/*     */       
/* 147 */       String data = node.getNodeValue();
/* 148 */       if ((data != null) && (data.length() > 0)) {
/* 149 */         this.out.print(' ');
/* 150 */         this.out.print(data);
/*     */       }
/* 152 */       this.out.print("?>");
/*     */     }
/*     */     
/*     */     
/* 156 */     if (type == 1) {
/* 157 */       this.out.print("</");
/* 158 */       this.out.print(node.getLocalName());
/* 159 */       this.out.print('>');
/*     */     }
/*     */     
/* 162 */     this.out.flush();
/*     */   }
/*     */   
/*     */ 
/*     */   private void printChildren(Node node)
/*     */   {
/* 168 */     NodeList children = node.getChildNodes();
/* 169 */     if (children != null) {
/* 170 */       int len = children.getLength();
/* 171 */       for (int i = 0; i < len; i++) {
/* 172 */         print(children.item(i));
/*     */       }
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
/*     */   @Deprecated
/*     */   protected Attr[] sortAttributes(NamedNodeMap attrs)
/*     */   {
/* 187 */     if (attrs == null) {
/* 188 */       return new Attr[0];
/*     */     }
/*     */     
/* 191 */     int len = attrs.getLength();
/* 192 */     Attr[] array = new Attr[len];
/* 193 */     for (int i = 0; i < len; i++) {
/* 194 */       array[i] = ((Attr)attrs.item(i));
/*     */     }
/* 196 */     for (int i = 0; i < len - 1; i++) {
/* 197 */       String name = null;
/* 198 */       name = array[i].getLocalName();
/* 199 */       int index = i;
/* 200 */       for (int j = i + 1; j < len; j++) {
/* 201 */         String curName = null;
/* 202 */         curName = array[j].getLocalName();
/* 203 */         if (curName.compareTo(name) < 0) {
/* 204 */           name = curName;
/* 205 */           index = j;
/*     */         }
/*     */       }
/* 208 */       if (index != i) {
/* 209 */         Attr temp = array[i];
/* 210 */         array[i] = array[index];
/* 211 */         array[index] = temp;
/*     */       }
/*     */     }
/*     */     
/* 215 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected String normalize(String s)
/*     */   {
/* 228 */     if (s == null) {
/* 229 */       return "";
/*     */     }
/*     */     
/* 232 */     StringBuilder str = new StringBuilder();
/*     */     
/* 234 */     int len = s.length();
/* 235 */     for (int i = 0; i < len; i++) {
/* 236 */       char ch = s.charAt(i);
/* 237 */       switch (ch) {
/*     */       case '<': 
/* 239 */         str.append("&lt;");
/* 240 */         break;
/*     */       case '>': 
/* 242 */         str.append("&gt;");
/* 243 */         break;
/*     */       case '&': 
/* 245 */         str.append("&amp;");
/* 246 */         break;
/*     */       case '"': 
/* 248 */         str.append("&quot;");
/* 249 */         break;
/*     */       case '\n': 
/*     */       case '\r': 
/* 252 */         if (this.canonical) {
/* 253 */           str.append("&#");
/* 254 */           str.append(Integer.toString(ch));
/* 255 */           str.append(';'); }
/* 256 */         break;
/*     */       }
/*     */       
/*     */       
/*     */ 
/* 261 */       str.append(ch);
/*     */     }
/*     */     
/*     */ 
/* 265 */     return str.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\DOMWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */