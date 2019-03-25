/*     */ package org.apache.naming;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class SelectorContext
/*     */   implements Context
/*     */ {
/*     */   public static final String prefix = "java:";
/*  54 */   public static final int prefixLength = "java:".length();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String IC_PREFIX = "IC_";
/*     */   
/*     */ 
/*     */ 
/*  63 */   private static final Log log = LogFactory.getLog(SelectorContext.class);
/*     */   
/*     */ 
/*     */ 
/*     */   protected final Hashtable<String, Object> env;
/*     */   
/*     */ 
/*     */ 
/*     */   public SelectorContext(Hashtable<String, Object> env)
/*     */   {
/*  73 */     this.env = env;
/*  74 */     this.initialContext = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelectorContext(Hashtable<String, Object> env, boolean initialContext)
/*     */   {
/*  86 */     this.env = env;
/*  87 */     this.initialContext = initialContext;
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
/* 103 */   protected static final StringManager sm = StringManager.getManager(SelectorContext.class);
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
/*     */   protected final boolean initialContext;
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
/*     */   public Object lookup(Name name)
/*     */     throws NamingException
/*     */   {
/* 132 */     if (log.isDebugEnabled()) {
/* 133 */       log.debug(sm.getString("selectorContext.methodUsingName", new Object[] { "lookup", name }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */     return getBoundContext().lookup(parseName(name));
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
/*     */   public Object lookup(String name)
/*     */     throws NamingException
/*     */   {
/* 155 */     if (log.isDebugEnabled()) {
/* 156 */       log.debug(sm.getString("selectorContext.methodUsingString", new Object[] { "lookup", name }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */     return getBoundContext().lookup(parseName(name));
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
/*     */   public void bind(Name name, Object obj)
/*     */     throws NamingException
/*     */   {
/* 183 */     getBoundContext().bind(parseName(name), obj);
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
/*     */   public void bind(String name, Object obj)
/*     */     throws NamingException
/*     */   {
/* 201 */     getBoundContext().bind(parseName(name), obj);
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
/*     */   public void rebind(Name name, Object obj)
/*     */     throws NamingException
/*     */   {
/* 223 */     getBoundContext().rebind(parseName(name), obj);
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
/*     */   public void rebind(String name, Object obj)
/*     */     throws NamingException
/*     */   {
/* 239 */     getBoundContext().rebind(parseName(name), obj);
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
/*     */   public void unbind(Name name)
/*     */     throws NamingException
/*     */   {
/* 260 */     getBoundContext().unbind(parseName(name));
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
/*     */   public void unbind(String name)
/*     */     throws NamingException
/*     */   {
/* 275 */     getBoundContext().unbind(parseName(name));
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
/*     */   public void rename(Name oldName, Name newName)
/*     */     throws NamingException
/*     */   {
/* 294 */     getBoundContext().rename(parseName(oldName), parseName(newName));
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
/*     */   public void rename(String oldName, String newName)
/*     */     throws NamingException
/*     */   {
/* 311 */     getBoundContext().rename(parseName(oldName), parseName(newName));
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
/*     */   public NamingEnumeration<NameClassPair> list(Name name)
/*     */     throws NamingException
/*     */   {
/* 332 */     if (log.isDebugEnabled()) {
/* 333 */       log.debug(sm.getString("selectorContext.methodUsingName", new Object[] { "list", name }));
/*     */     }
/*     */     
/*     */ 
/* 337 */     return getBoundContext().list(parseName(name));
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
/*     */   public NamingEnumeration<NameClassPair> list(String name)
/*     */     throws NamingException
/*     */   {
/* 354 */     if (log.isDebugEnabled()) {
/* 355 */       log.debug(sm.getString("selectorContext.methodUsingString", new Object[] { "list", name }));
/*     */     }
/*     */     
/*     */ 
/* 359 */     return getBoundContext().list(parseName(name));
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
/*     */   public NamingEnumeration<Binding> listBindings(Name name)
/*     */     throws NamingException
/*     */   {
/* 380 */     if (log.isDebugEnabled()) {
/* 381 */       log.debug(sm.getString("selectorContext.methodUsingName", new Object[] { "listBindings", name }));
/*     */     }
/*     */     
/*     */ 
/* 385 */     return getBoundContext().listBindings(parseName(name));
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
/*     */   public NamingEnumeration<Binding> listBindings(String name)
/*     */     throws NamingException
/*     */   {
/* 402 */     if (log.isDebugEnabled()) {
/* 403 */       log.debug(sm.getString("selectorContext.methodUsingString", new Object[] { "listBindings", name }));
/*     */     }
/*     */     
/*     */ 
/* 407 */     return getBoundContext().listBindings(parseName(name));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroySubcontext(Name name)
/*     */     throws NamingException
/*     */   {
/* 439 */     getBoundContext().destroySubcontext(parseName(name));
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
/*     */   public void destroySubcontext(String name)
/*     */     throws NamingException
/*     */   {
/* 455 */     getBoundContext().destroySubcontext(parseName(name));
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
/*     */   public Context createSubcontext(Name name)
/*     */     throws NamingException
/*     */   {
/* 476 */     return getBoundContext().createSubcontext(parseName(name));
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
/*     */   public Context createSubcontext(String name)
/*     */     throws NamingException
/*     */   {
/* 494 */     return getBoundContext().createSubcontext(parseName(name));
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
/*     */   public Object lookupLink(Name name)
/*     */     throws NamingException
/*     */   {
/* 512 */     if (log.isDebugEnabled()) {
/* 513 */       log.debug(sm.getString("selectorContext.methodUsingName", new Object[] { "lookupLink", name }));
/*     */     }
/*     */     
/*     */ 
/* 517 */     return getBoundContext().lookupLink(parseName(name));
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
/*     */   public Object lookupLink(String name)
/*     */     throws NamingException
/*     */   {
/* 534 */     if (log.isDebugEnabled()) {
/* 535 */       log.debug(sm.getString("selectorContext.methodUsingString", new Object[] { "lookupLink", name }));
/*     */     }
/*     */     
/*     */ 
/* 539 */     return getBoundContext().lookupLink(parseName(name));
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
/*     */   public NameParser getNameParser(Name name)
/*     */     throws NamingException
/*     */   {
/* 560 */     return getBoundContext().getNameParser(parseName(name));
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
/*     */   public NameParser getNameParser(String name)
/*     */     throws NamingException
/*     */   {
/* 575 */     return getBoundContext().getNameParser(parseName(name));
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
/*     */   public Name composeName(Name name, Name prefix)
/*     */     throws NamingException
/*     */   {
/* 597 */     Name prefixClone = (Name)prefix.clone();
/* 598 */     return prefixClone.addAll(name);
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
/*     */   public String composeName(String name, String prefix)
/*     */     throws NamingException
/*     */   {
/* 613 */     return prefix + "/" + name;
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
/*     */   public Object addToEnvironment(String propName, Object propVal)
/*     */     throws NamingException
/*     */   {
/* 629 */     return getBoundContext().addToEnvironment(propName, propVal);
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
/*     */   public Object removeFromEnvironment(String propName)
/*     */     throws NamingException
/*     */   {
/* 643 */     return getBoundContext().removeFromEnvironment(propName);
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
/*     */   public Hashtable<?, ?> getEnvironment()
/*     */     throws NamingException
/*     */   {
/* 660 */     return getBoundContext().getEnvironment();
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
/*     */   public void close()
/*     */     throws NamingException
/*     */   {
/* 677 */     getBoundContext().close();
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
/*     */   public String getNameInNamespace()
/*     */     throws NamingException
/*     */   {
/* 701 */     return "java:";
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
/*     */   protected Context getBoundContext()
/*     */     throws NamingException
/*     */   {
/* 717 */     if (this.initialContext) {
/* 718 */       String ICName = "IC_";
/* 719 */       if (ContextBindings.isThreadBound()) {
/* 720 */         ICName = ICName + ContextBindings.getThreadName();
/* 721 */       } else if (ContextBindings.isClassLoaderBound()) {
/* 722 */         ICName = ICName + ContextBindings.getClassLoaderName();
/*     */       }
/* 724 */       Context initialContext = ContextBindings.getContext(ICName);
/* 725 */       if (initialContext == null)
/*     */       {
/*     */ 
/* 728 */         initialContext = new NamingContext(this.env, ICName);
/* 729 */         ContextBindings.bindContext(ICName, initialContext);
/*     */       }
/* 731 */       return initialContext;
/*     */     }
/* 733 */     if (ContextBindings.isThreadBound()) {
/* 734 */       return ContextBindings.getThread();
/*     */     }
/* 736 */     return ContextBindings.getClassLoader();
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
/*     */   protected String parseName(String name)
/*     */     throws NamingException
/*     */   {
/* 753 */     if ((!this.initialContext) && (name.startsWith("java:"))) {
/* 754 */       return name.substring(prefixLength);
/*     */     }
/* 756 */     if (this.initialContext) {
/* 757 */       return name;
/*     */     }
/*     */     
/* 760 */     throw new NamingException(sm.getString("selectorContext.noJavaUrl"));
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
/*     */   protected Name parseName(Name name)
/*     */     throws NamingException
/*     */   {
/* 777 */     if ((!this.initialContext) && (!name.isEmpty()) && 
/* 778 */       (name.get(0).startsWith("java:"))) {
/* 779 */       if (name.get(0).equals("java:")) {
/* 780 */         return name.getSuffix(1);
/*     */       }
/* 782 */       Name result = name.getSuffix(1);
/* 783 */       result.add(0, name.get(0).substring(prefixLength));
/* 784 */       return result;
/*     */     }
/*     */     
/* 787 */     if (this.initialContext) {
/* 788 */       return name;
/*     */     }
/*     */     
/* 791 */     throw new NamingException(sm.getString("selectorContext.noJavaUrl"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\SelectorContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */