/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import org.apache.catalina.Contained;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.util.LifecycleMBeanBase;
/*     */ import org.apache.juli.logging.Log;
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
/*     */ public abstract class ValveBase
/*     */   extends LifecycleMBeanBase
/*     */   implements Contained, Valve
/*     */ {
/*  40 */   protected static final StringManager sm = StringManager.getManager(ValveBase.class);
/*     */   
/*     */   protected boolean asyncSupported;
/*     */   
/*     */   public ValveBase()
/*     */   {
/*  46 */     this(false);
/*     */   }
/*     */   
/*     */   public ValveBase(boolean asyncSupported)
/*     */   {
/*  51 */     this.asyncSupported = asyncSupported;
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
/*  66 */   protected Container container = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   protected Log containerLog = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   protected Valve next = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Container getContainer()
/*     */   {
/*  88 */     return this.container;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContainer(Container container)
/*     */   {
/*  99 */     this.container = container;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isAsyncSupported()
/*     */   {
/* 105 */     return this.asyncSupported;
/*     */   }
/*     */   
/*     */   public void setAsyncSupported(boolean asyncSupported)
/*     */   {
/* 110 */     this.asyncSupported = asyncSupported;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Valve getNext()
/*     */   {
/* 120 */     return this.next;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNext(Valve valve)
/*     */   {
/* 131 */     this.next = valve;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void backgroundProcess() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 150 */     super.initInternal();
/* 151 */     this.containerLog = getContainer().getLogger();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 164 */     setState(LifecycleState.STARTING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 177 */     setState(LifecycleState.STOPPING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 186 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 187 */     sb.append('[');
/* 188 */     if (this.container == null) {
/* 189 */       sb.append("Container is null");
/*     */     } else {
/* 191 */       sb.append(this.container.getName());
/*     */     }
/* 193 */     sb.append(']');
/* 194 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectNameKeyProperties()
/*     */   {
/* 202 */     StringBuilder name = new StringBuilder("type=Valve");
/*     */     
/* 204 */     Container container = getContainer();
/*     */     
/* 206 */     name.append(container.getMBeanKeyProperties());
/*     */     
/* 208 */     int seq = 0;
/*     */     
/*     */ 
/* 211 */     Pipeline p = container.getPipeline();
/* 212 */     if (p != null) {
/* 213 */       for (Valve valve : p.getValves())
/*     */       {
/* 215 */         if (valve != null)
/*     */         {
/*     */ 
/*     */ 
/* 219 */           if (valve == this) {
/*     */             break;
/*     */           }
/* 222 */           if (valve.getClass() == getClass())
/*     */           {
/*     */ 
/* 225 */             seq++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 230 */     if (seq > 0) {
/* 231 */       name.append(",seq=");
/* 232 */       name.append(seq);
/*     */     }
/*     */     
/* 235 */     String className = getClass().getName();
/* 236 */     int period = className.lastIndexOf('.');
/* 237 */     if (period >= 0) {
/* 238 */       className = className.substring(period + 1);
/*     */     }
/* 240 */     name.append(",name=");
/* 241 */     name.append(className);
/*     */     
/* 243 */     return name.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDomainInternal()
/*     */   {
/* 249 */     Container c = getContainer();
/* 250 */     if (c == null) {
/* 251 */       return null;
/*     */     }
/* 253 */     return c.getDomain();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\ValveBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */