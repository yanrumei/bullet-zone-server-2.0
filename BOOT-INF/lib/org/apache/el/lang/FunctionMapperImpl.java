/*     */ package org.apache.el.lang;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.el.FunctionMapper;
/*     */ import org.apache.el.util.ReflectionUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FunctionMapperImpl
/*     */   extends FunctionMapper
/*     */   implements Externalizable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  40 */   protected ConcurrentMap<String, Function> functions = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Method resolveFunction(String prefix, String localName)
/*     */   {
/*  50 */     Function f = (Function)this.functions.get(prefix + ":" + localName);
/*  51 */     if (f == null) {
/*  52 */       return null;
/*     */     }
/*  54 */     return f.getMethod();
/*     */   }
/*     */   
/*     */   public void mapFunction(String prefix, String localName, Method m)
/*     */   {
/*  59 */     String key = prefix + ":" + localName;
/*  60 */     if (m == null) {
/*  61 */       this.functions.remove(key);
/*     */     } else {
/*  63 */       Function f = new Function(prefix, localName, m);
/*  64 */       this.functions.put(key, f);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeExternal(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/*  75 */     out.writeObject(this.functions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readExternal(ObjectInput in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  87 */     this.functions = ((ConcurrentMap)in.readObject());
/*     */   }
/*     */   
/*     */   public static class Function implements Externalizable
/*     */   {
/*     */     protected transient Method m;
/*     */     protected String owner;
/*     */     protected String name;
/*     */     protected String[] types;
/*     */     protected String prefix;
/*     */     protected String localName;
/*     */     
/*     */     public Function(String prefix, String localName, Method m) {
/* 100 */       if (localName == null) {
/* 101 */         throw new NullPointerException("LocalName cannot be null");
/*     */       }
/* 103 */       if (m == null) {
/* 104 */         throw new NullPointerException("Method cannot be null");
/*     */       }
/* 106 */       this.prefix = prefix;
/* 107 */       this.localName = localName;
/* 108 */       this.m = m;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Function() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void writeExternal(ObjectOutput out)
/*     */       throws IOException
/*     */     {
/* 122 */       out.writeUTF(this.prefix != null ? this.prefix : "");
/* 123 */       out.writeUTF(this.localName);
/*     */       
/* 125 */       getMethod();
/* 126 */       out.writeUTF(this.owner != null ? this.owner : this.m
/*     */       
/* 128 */         .getDeclaringClass().getName());
/* 129 */       out.writeUTF(this.name != null ? this.name : this.m
/*     */       
/* 131 */         .getName());
/* 132 */       out.writeObject(this.types != null ? this.types : 
/*     */       
/* 134 */         ReflectionUtil.toTypeNameArray(this.m.getParameterTypes()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void readExternal(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 147 */       this.prefix = in.readUTF();
/* 148 */       if ("".equals(this.prefix)) this.prefix = null;
/* 149 */       this.localName = in.readUTF();
/* 150 */       this.owner = in.readUTF();
/* 151 */       this.name = in.readUTF();
/* 152 */       this.types = ((String[])in.readObject());
/*     */     }
/*     */     
/*     */     public Method getMethod() {
/* 156 */       if (this.m == null) {
/*     */         try {
/* 158 */           Class<?> t = ReflectionUtil.forName(this.owner);
/* 159 */           Class<?>[] p = ReflectionUtil.toTypeArray(this.types);
/* 160 */           this.m = t.getMethod(this.name, p);
/*     */         } catch (Exception e) {
/* 162 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 165 */       return this.m;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(Object obj)
/*     */     {
/* 173 */       if ((obj instanceof Function)) {
/* 174 */         return hashCode() == obj.hashCode();
/*     */       }
/* 176 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 184 */       return (this.prefix + this.localName).hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\lang\FunctionMapperImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */