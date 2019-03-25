/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Set;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.catalina.Contained;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.JmxEnabled;
/*     */ import org.apache.catalina.Lifecycle;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.util.LifecycleBase;
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
/*     */ public class StandardPipeline
/*     */   extends LifecycleBase
/*     */   implements Pipeline, Contained
/*     */ {
/*  57 */   private static final Log log = LogFactory.getLog(StandardPipeline.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardPipeline()
/*     */   {
/*  67 */     this(null);
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
/*     */   public StandardPipeline(Container container)
/*     */   {
/*  81 */     setContainer(container);
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
/*  92 */   protected Valve basic = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */   protected Container container = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 104 */   protected Valve first = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAsyncSupported()
/*     */   {
/* 111 */     Valve valve = this.first != null ? this.first : this.basic;
/* 112 */     boolean supported = true;
/* 113 */     while ((supported) && (valve != null)) {
/* 114 */       supported &= valve.isAsyncSupported();
/* 115 */       valve = valve.getNext();
/*     */     }
/* 117 */     return supported;
/*     */   }
/*     */   
/*     */ 
/*     */   public void findNonAsyncValves(Set<String> result)
/*     */   {
/* 123 */     Valve valve = this.first != null ? this.first : this.basic;
/* 124 */     while (valve != null) {
/* 125 */       if (!valve.isAsyncSupported()) {
/* 126 */         result.add(valve.getClass().getName());
/*     */       }
/* 128 */       valve = valve.getNext();
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
/*     */   public Container getContainer()
/*     */   {
/* 141 */     return this.container;
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
/*     */   public void setContainer(Container container)
/*     */   {
/* 154 */     this.container = container;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal() {}
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
/* 176 */     Valve current = this.first;
/* 177 */     if (current == null) {
/* 178 */       current = this.basic;
/*     */     }
/* 180 */     while (current != null) {
/* 181 */       if ((current instanceof Lifecycle))
/* 182 */         ((Lifecycle)current).start();
/* 183 */       current = current.getNext();
/*     */     }
/*     */     
/* 186 */     setState(LifecycleState.STARTING);
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
/*     */   protected synchronized void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 200 */     setState(LifecycleState.STOPPING);
/*     */     
/*     */ 
/* 203 */     Valve current = this.first;
/* 204 */     if (current == null) {
/* 205 */       current = this.basic;
/*     */     }
/* 207 */     while (current != null) {
/* 208 */       if ((current instanceof Lifecycle))
/* 209 */         ((Lifecycle)current).stop();
/* 210 */       current = current.getNext();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void destroyInternal()
/*     */   {
/* 217 */     Valve[] valves = getValves();
/* 218 */     for (Valve valve : valves) {
/* 219 */       removeValve(valve);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 229 */     StringBuilder sb = new StringBuilder("Pipeline[");
/* 230 */     sb.append(this.container);
/* 231 */     sb.append(']');
/* 232 */     return sb.toString();
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
/*     */   public Valve getBasic()
/*     */   {
/* 246 */     return this.basic;
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
/*     */ 
/*     */ 
/*     */   public void setBasic(Valve valve)
/*     */   {
/* 267 */     Valve oldBasic = this.basic;
/* 268 */     if (oldBasic == valve) {
/* 269 */       return;
/*     */     }
/*     */     
/* 272 */     if (oldBasic != null) {
/* 273 */       if ((getState().isAvailable()) && ((oldBasic instanceof Lifecycle))) {
/*     */         try {
/* 275 */           ((Lifecycle)oldBasic).stop();
/*     */         } catch (LifecycleException e) {
/* 277 */           log.error("StandardPipeline.setBasic: stop", e);
/*     */         }
/*     */       }
/* 280 */       if ((oldBasic instanceof Contained)) {
/*     */         try {
/* 282 */           ((Contained)oldBasic).setContainer(null);
/*     */         } catch (Throwable t) {
/* 284 */           ExceptionUtils.handleThrowable(t);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 290 */     if (valve == null)
/* 291 */       return;
/* 292 */     if ((valve instanceof Contained)) {
/* 293 */       ((Contained)valve).setContainer(this.container);
/*     */     }
/* 295 */     if ((getState().isAvailable()) && ((valve instanceof Lifecycle))) {
/*     */       try {
/* 297 */         ((Lifecycle)valve).start();
/*     */       } catch (LifecycleException e) {
/* 299 */         log.error("StandardPipeline.setBasic: start", e);
/* 300 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 305 */     Valve current = this.first;
/* 306 */     while (current != null) {
/* 307 */       if (current.getNext() == oldBasic) {
/* 308 */         current.setNext(valve);
/* 309 */         break;
/*     */       }
/* 311 */       current = current.getNext();
/*     */     }
/*     */     
/* 314 */     this.basic = valve;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addValve(Valve valve)
/*     */   {
/* 342 */     if ((valve instanceof Contained)) {
/* 343 */       ((Contained)valve).setContainer(this.container);
/*     */     }
/*     */     
/* 346 */     if ((getState().isAvailable()) && 
/* 347 */       ((valve instanceof Lifecycle))) {
/*     */       try {
/* 349 */         ((Lifecycle)valve).start();
/*     */       } catch (LifecycleException e) {
/* 351 */         log.error("StandardPipeline.addValve: start: ", e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 357 */     if (this.first == null) {
/* 358 */       this.first = valve;
/* 359 */       valve.setNext(this.basic);
/*     */     } else {
/* 361 */       Valve current = this.first;
/* 362 */       while (current != null) {
/* 363 */         if (current.getNext() == this.basic) {
/* 364 */           current.setNext(valve);
/* 365 */           valve.setNext(this.basic);
/* 366 */           break;
/*     */         }
/* 368 */         current = current.getNext();
/*     */       }
/*     */     }
/*     */     
/* 372 */     this.container.fireContainerEvent("addValve", valve);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Valve[] getValves()
/*     */   {
/* 384 */     ArrayList<Valve> valveList = new ArrayList();
/* 385 */     Valve current = this.first;
/* 386 */     if (current == null) {
/* 387 */       current = this.basic;
/*     */     }
/* 389 */     while (current != null) {
/* 390 */       valveList.add(current);
/* 391 */       current = current.getNext();
/*     */     }
/*     */     
/* 394 */     return (Valve[])valveList.toArray(new Valve[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectName[] getValveObjectNames()
/*     */   {
/* 400 */     ArrayList<ObjectName> valveList = new ArrayList();
/* 401 */     Valve current = this.first;
/* 402 */     if (current == null) {
/* 403 */       current = this.basic;
/*     */     }
/* 405 */     while (current != null) {
/* 406 */       if ((current instanceof JmxEnabled)) {
/* 407 */         valveList.add(((JmxEnabled)current).getObjectName());
/*     */       }
/* 409 */       current = current.getNext();
/*     */     }
/*     */     
/* 412 */     return (ObjectName[])valveList.toArray(new ObjectName[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeValve(Valve valve)
/*     */   {
/*     */     Valve current;
/*     */     
/*     */ 
/*     */ 
/*     */     Valve current;
/*     */     
/*     */ 
/*     */ 
/* 428 */     if (this.first == valve) {
/* 429 */       this.first = this.first.getNext();
/* 430 */       current = null;
/*     */     } else {
/* 432 */       current = this.first;
/*     */     }
/* 434 */     while (current != null) {
/* 435 */       if (current.getNext() == valve) {
/* 436 */         current.setNext(valve.getNext());
/* 437 */         break;
/*     */       }
/* 439 */       current = current.getNext();
/*     */     }
/*     */     
/* 442 */     if (this.first == this.basic) { this.first = null;
/*     */     }
/* 444 */     if ((valve instanceof Contained)) {
/* 445 */       ((Contained)valve).setContainer(null);
/*     */     }
/* 447 */     if ((valve instanceof Lifecycle))
/*     */     {
/* 449 */       if (getState().isAvailable()) {
/*     */         try {
/* 451 */           ((Lifecycle)valve).stop();
/*     */         } catch (LifecycleException e) {
/* 453 */           log.error("StandardPipeline.removeValve: stop: ", e);
/*     */         }
/*     */       }
/*     */       try {
/* 457 */         ((Lifecycle)valve).destroy();
/*     */       } catch (LifecycleException e) {
/* 459 */         log.error("StandardPipeline.removeValve: destroy: ", e);
/*     */       }
/*     */     }
/*     */     
/* 463 */     this.container.fireContainerEvent("removeValve", valve);
/*     */   }
/*     */   
/*     */ 
/*     */   public Valve getFirst()
/*     */   {
/* 469 */     if (this.first != null) {
/* 470 */       return this.first;
/*     */     }
/*     */     
/* 473 */     return this.basic;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardPipeline.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */