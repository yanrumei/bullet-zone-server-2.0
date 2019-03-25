/*    */ package org.apache.tomcat.util.descriptor.web;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.buf.B2CConverter;
/*    */ import org.apache.tomcat.util.res.StringManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class XmlEncodingBase
/*    */ {
/* 34 */   private static final Log log = LogFactory.getLog(XmlEncodingBase.class);
/* 35 */   private static final StringManager sm = StringManager.getManager(XmlEncodingBase.class);
/* 36 */   private Charset charset = StandardCharsets.UTF_8;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public void setEncoding(String encoding)
/*    */   {
/*    */     try
/*    */     {
/* 47 */       this.charset = B2CConverter.getCharset(encoding);
/*    */     } catch (UnsupportedEncodingException e) {
/* 49 */       log.warn(sm.getString("xmlEncodingBase.encodingInvalid", new Object[] { encoding, this.charset.name() }), e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public String getEncoding()
/*    */   {
/* 64 */     return this.charset.name();
/*    */   }
/*    */   
/*    */   public void setCharset(Charset charset)
/*    */   {
/* 69 */     this.charset = charset;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Charset getCharset()
/*    */   {
/* 81 */     return this.charset;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\XmlEncodingBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */