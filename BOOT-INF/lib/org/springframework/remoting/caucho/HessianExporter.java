/*     */ package org.springframework.remoting.caucho;
/*     */ 
/*     */ import com.caucho.hessian.io.AbstractHessianInput;
/*     */ import com.caucho.hessian.io.AbstractHessianOutput;
/*     */ import com.caucho.hessian.io.Hessian2Input;
/*     */ import com.caucho.hessian.io.Hessian2Output;
/*     */ import com.caucho.hessian.io.HessianDebugInputStream;
/*     */ import com.caucho.hessian.io.HessianDebugOutputStream;
/*     */ import com.caucho.hessian.io.HessianInput;
/*     */ import com.caucho.hessian.io.HessianOutput;
/*     */ import com.caucho.hessian.io.HessianRemoteResolver;
/*     */ import com.caucho.hessian.io.SerializerFactory;
/*     */ import com.caucho.hessian.server.HessianSkeleton;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.remoting.support.RemoteExporter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CommonsLogWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HessianExporter
/*     */   extends RemoteExporter
/*     */   implements InitializingBean
/*     */ {
/*     */   public static final String CONTENT_TYPE_HESSIAN = "application/x-hessian";
/*  62 */   private SerializerFactory serializerFactory = new SerializerFactory();
/*     */   
/*     */ 
/*     */ 
/*     */   private HessianRemoteResolver remoteResolver;
/*     */   
/*     */ 
/*     */   private Log debugLogger;
/*     */   
/*     */ 
/*     */   private HessianSkeleton skeleton;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSerializerFactory(SerializerFactory serializerFactory)
/*     */   {
/*  78 */     this.serializerFactory = (serializerFactory != null ? serializerFactory : new SerializerFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSendCollectionType(boolean sendCollectionType)
/*     */   {
/*  86 */     this.serializerFactory.setSendCollectionType(sendCollectionType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowNonSerializable(boolean allowNonSerializable)
/*     */   {
/*  94 */     this.serializerFactory.setAllowNonSerializable(allowNonSerializable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoteResolver(HessianRemoteResolver remoteResolver)
/*     */   {
/* 102 */     this.remoteResolver = remoteResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDebug(boolean debug)
/*     */   {
/* 111 */     this.debugLogger = (debug ? this.logger : null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 117 */     prepare();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void prepare()
/*     */   {
/* 124 */     checkService();
/* 125 */     checkServiceInterface();
/* 126 */     this.skeleton = new HessianSkeleton(getProxyForService(), getServiceInterface());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invoke(InputStream inputStream, OutputStream outputStream)
/*     */     throws Throwable
/*     */   {
/* 137 */     Assert.notNull(this.skeleton, "Hessian exporter has not been initialized");
/* 138 */     doInvoke(this.skeleton, inputStream, outputStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doInvoke(HessianSkeleton skeleton, InputStream inputStream, OutputStream outputStream)
/*     */     throws Throwable
/*     */   {
/* 151 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/*     */     try {
/* 153 */       InputStream isToUse = inputStream;
/* 154 */       OutputStream osToUse = outputStream;
/*     */       
/* 156 */       if ((this.debugLogger != null) && (this.debugLogger.isDebugEnabled())) {
/* 157 */         PrintWriter debugWriter = new PrintWriter(new CommonsLogWriter(this.debugLogger));
/*     */         
/* 159 */         HessianDebugInputStream dis = new HessianDebugInputStream(inputStream, debugWriter);
/*     */         
/* 161 */         HessianDebugOutputStream dos = new HessianDebugOutputStream(outputStream, debugWriter);
/* 162 */         dis.startTop2();
/* 163 */         dos.startTop2();
/* 164 */         isToUse = dis;
/* 165 */         osToUse = dos;
/*     */       }
/*     */       
/* 168 */       if (!isToUse.markSupported()) {
/* 169 */         isToUse = new BufferedInputStream(isToUse);
/* 170 */         isToUse.mark(1);
/*     */       }
/*     */       
/* 173 */       int code = isToUse.read();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */       if (code == 72)
/*     */       {
/* 182 */         int major = isToUse.read();
/* 183 */         int minor = isToUse.read();
/* 184 */         if (major != 2) {
/* 185 */           throw new IOException("Version " + major + '.' + minor + " is not understood");
/*     */         }
/* 187 */         AbstractHessianInput in = new Hessian2Input(isToUse);
/* 188 */         AbstractHessianOutput out = new Hessian2Output(osToUse);
/* 189 */         in.readCall();
/*     */       }
/* 191 */       else if (code == 67)
/*     */       {
/* 193 */         isToUse.reset();
/* 194 */         AbstractHessianInput in = new Hessian2Input(isToUse);
/* 195 */         AbstractHessianOutput out = new Hessian2Output(osToUse);
/* 196 */         in.readCall();
/*     */       } else { AbstractHessianOutput out;
/* 198 */         if (code == 99)
/*     */         {
/* 200 */           int major = isToUse.read();
/* 201 */           int minor = isToUse.read();
/* 202 */           AbstractHessianInput in = new HessianInput(isToUse);
/* 203 */           AbstractHessianOutput out; if (major >= 2) {
/* 204 */             out = new Hessian2Output(osToUse);
/*     */           }
/*     */           else {
/* 207 */             out = new HessianOutput(osToUse);
/*     */           }
/*     */         }
/*     */         else {
/* 211 */           throw new IOException("Expected 'H'/'C' (Hessian 2.0) or 'c' (Hessian 1.0) in hessian input at " + code); } }
/*     */       AbstractHessianOutput out;
/*     */       AbstractHessianInput in;
/* 214 */       if (this.serializerFactory != null) {
/* 215 */         in.setSerializerFactory(this.serializerFactory);
/* 216 */         out.setSerializerFactory(this.serializerFactory);
/*     */       }
/* 218 */       if (this.remoteResolver != null) {
/* 219 */         in.setRemoteResolver(this.remoteResolver);
/*     */       }
/*     */       try
/*     */       {
/* 223 */         skeleton.invoke(in, out);
/*     */         
/*     */         try
/*     */         {
/* 227 */           in.close();
/* 228 */           isToUse.close();
/*     */         }
/*     */         catch (IOException localIOException) {}
/*     */         
/*     */         try
/*     */         {
/* 234 */           out.close();
/* 235 */           osToUse.close();
/*     */         }
/*     */         catch (IOException localIOException1) {}
/*     */       }
/*     */       finally
/*     */       {
/*     */         try
/*     */         {
/* 227 */           in.close();
/* 228 */           isToUse.close();
/*     */         }
/*     */         catch (IOException localIOException2) {}
/*     */         
/*     */         try
/*     */         {
/* 234 */           out.close();
/* 235 */           osToUse.close();
/*     */ 
/*     */         }
/*     */         catch (IOException localIOException3) {}
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 243 */       resetThreadContextClassLoader(originalClassLoader);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\caucho\HessianExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */