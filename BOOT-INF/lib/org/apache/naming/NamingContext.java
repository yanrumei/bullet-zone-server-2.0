/*     */ package org.apache.naming;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.LinkRef;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameAlreadyBoundException;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NotContextException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.naming.spi.NamingManager;
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
/*     */ public class NamingContext
/*     */   implements Context
/*     */ {
/*  60 */   protected static final NameParser nameParser = new NameParserImpl();
/*     */   
/*     */ 
/*  63 */   private static final Log log = LogFactory.getLog(NamingContext.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Hashtable<String, Object> env;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamingContext(Hashtable<String, Object> env, String name)
/*     */   {
/*  76 */     this(env, name, new HashMap());
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
/*     */   public NamingContext(Hashtable<String, Object> env, String name, HashMap<String, NamingEntry> bindings)
/*     */   {
/*  90 */     this.env = new Hashtable();
/*  91 */     this.name = name;
/*     */     
/*  93 */     if (env != null) {
/*  94 */       Enumeration<String> envEntries = env.keys();
/*  95 */       while (envEntries.hasMoreElements()) {
/*  96 */         String entryName = (String)envEntries.nextElement();
/*  97 */         addToEnvironment(entryName, env.get(entryName));
/*     */       }
/*     */     }
/* 100 */     this.bindings = bindings;
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
/* 116 */   protected static final StringManager sm = StringManager.getManager(NamingContext.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final HashMap<String, NamingEntry> bindings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */   private boolean exceptionOnFailedWrite = true;
/*     */   
/* 137 */   public boolean getExceptionOnFailedWrite() { return this.exceptionOnFailedWrite; }
/*     */   
/*     */   public void setExceptionOnFailedWrite(boolean exceptionOnFailedWrite) {
/* 140 */     this.exceptionOnFailedWrite = exceptionOnFailedWrite;
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
/*     */   public Object lookup(Name name)
/*     */     throws NamingException
/*     */   {
/* 159 */     return lookup(name, true);
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
/*     */   public Object lookup(String name)
/*     */     throws NamingException
/*     */   {
/* 173 */     return lookup(new CompositeName(name), true);
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
/*     */   public void bind(Name name, Object obj)
/*     */     throws NamingException
/*     */   {
/* 192 */     bind(name, obj, false);
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
/*     */   public void bind(String name, Object obj)
/*     */     throws NamingException
/*     */   {
/* 209 */     bind(new CompositeName(name), obj);
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
/* 231 */     bind(name, obj, true);
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
/* 247 */     rebind(new CompositeName(name), obj);
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
/* 268 */     if (!checkWritable()) {
/* 269 */       return;
/*     */     }
/*     */     
/* 272 */     while ((!name.isEmpty()) && (name.get(0).length() == 0))
/* 273 */       name = name.getSuffix(1);
/* 274 */     if (name.isEmpty())
/*     */     {
/* 276 */       throw new NamingException(sm.getString("namingContext.invalidName"));
/*     */     }
/* 278 */     NamingEntry entry = (NamingEntry)this.bindings.get(name.get(0));
/*     */     
/* 280 */     if (entry == null)
/*     */     {
/* 282 */       throw new NameNotFoundException(sm.getString("namingContext.nameNotBound", new Object[] { name, name.get(0) }));
/*     */     }
/*     */     
/* 285 */     if (name.size() > 1) {
/* 286 */       if (entry.type == 10) {
/* 287 */         ((Context)entry.value).unbind(name.getSuffix(1));
/*     */       }
/*     */       else {
/* 290 */         throw new NamingException(sm.getString("namingContext.contextExpected"));
/*     */       }
/*     */     } else {
/* 293 */       this.bindings.remove(name.get(0));
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
/*     */ 
/*     */   public void unbind(String name)
/*     */     throws NamingException
/*     */   {
/* 310 */     unbind(new CompositeName(name));
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
/*     */   public void rename(Name oldName, Name newName)
/*     */     throws NamingException
/*     */   {
/* 328 */     Object value = lookup(oldName);
/* 329 */     bind(newName, value);
/* 330 */     unbind(oldName);
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
/*     */   public void rename(String oldName, String newName)
/*     */     throws NamingException
/*     */   {
/* 346 */     rename(new CompositeName(oldName), new CompositeName(newName));
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
/* 367 */     while ((!name.isEmpty()) && (name.get(0).length() == 0))
/* 368 */       name = name.getSuffix(1);
/* 369 */     if (name.isEmpty()) {
/* 370 */       return new NamingContextEnumeration(this.bindings.values().iterator());
/*     */     }
/*     */     
/* 373 */     NamingEntry entry = (NamingEntry)this.bindings.get(name.get(0));
/*     */     
/* 375 */     if (entry == null)
/*     */     {
/* 377 */       throw new NameNotFoundException(sm.getString("namingContext.nameNotBound", new Object[] { name, name.get(0) }));
/*     */     }
/*     */     
/* 380 */     if (entry.type != 10)
/*     */     {
/* 382 */       throw new NamingException(sm.getString("namingContext.contextExpected"));
/*     */     }
/* 384 */     return ((Context)entry.value).list(name.getSuffix(1));
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
/*     */   public NamingEnumeration<NameClassPair> list(String name)
/*     */     throws NamingException
/*     */   {
/* 400 */     return list(new CompositeName(name));
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
/* 421 */     while ((!name.isEmpty()) && (name.get(0).length() == 0))
/* 422 */       name = name.getSuffix(1);
/* 423 */     if (name.isEmpty()) {
/* 424 */       return new NamingContextBindingsEnumeration(this.bindings.values().iterator(), this);
/*     */     }
/*     */     
/* 427 */     NamingEntry entry = (NamingEntry)this.bindings.get(name.get(0));
/*     */     
/* 429 */     if (entry == null)
/*     */     {
/* 431 */       throw new NameNotFoundException(sm.getString("namingContext.nameNotBound", new Object[] { name, name.get(0) }));
/*     */     }
/*     */     
/* 434 */     if (entry.type != 10)
/*     */     {
/* 436 */       throw new NamingException(sm.getString("namingContext.contextExpected"));
/*     */     }
/* 438 */     return ((Context)entry.value).listBindings(name.getSuffix(1));
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
/*     */   public NamingEnumeration<Binding> listBindings(String name)
/*     */     throws NamingException
/*     */   {
/* 454 */     return listBindings(new CompositeName(name));
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
/* 486 */     if (!checkWritable()) {
/* 487 */       return;
/*     */     }
/*     */     
/* 490 */     while ((!name.isEmpty()) && (name.get(0).length() == 0))
/* 491 */       name = name.getSuffix(1);
/* 492 */     if (name.isEmpty())
/*     */     {
/* 494 */       throw new NamingException(sm.getString("namingContext.invalidName"));
/*     */     }
/* 496 */     NamingEntry entry = (NamingEntry)this.bindings.get(name.get(0));
/*     */     
/* 498 */     if (entry == null)
/*     */     {
/* 500 */       throw new NameNotFoundException(sm.getString("namingContext.nameNotBound", new Object[] { name, name.get(0) }));
/*     */     }
/*     */     
/* 503 */     if (name.size() > 1) {
/* 504 */       if (entry.type == 10) {
/* 505 */         ((Context)entry.value).destroySubcontext(name.getSuffix(1));
/*     */       }
/*     */       else {
/* 508 */         throw new NamingException(sm.getString("namingContext.contextExpected"));
/*     */       }
/*     */     }
/* 511 */     else if (entry.type == 10) {
/* 512 */       ((Context)entry.value).close();
/* 513 */       this.bindings.remove(name.get(0));
/*     */     }
/*     */     else {
/* 516 */       throw new NotContextException(sm.getString("namingContext.contextExpected"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroySubcontext(String name)
/*     */     throws NamingException
/*     */   {
/* 535 */     destroySubcontext(new CompositeName(name));
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
/*     */   public Context createSubcontext(Name name)
/*     */     throws NamingException
/*     */   {
/* 554 */     if (!checkWritable()) {
/* 555 */       return null;
/*     */     }
/*     */     
/* 558 */     NamingContext newContext = new NamingContext(this.env, this.name);
/* 559 */     bind(name, newContext);
/*     */     
/* 561 */     newContext.setExceptionOnFailedWrite(getExceptionOnFailedWrite());
/*     */     
/* 563 */     return newContext;
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
/*     */   public Context createSubcontext(String name)
/*     */     throws NamingException
/*     */   {
/* 580 */     return createSubcontext(new CompositeName(name));
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
/*     */   public Object lookupLink(Name name)
/*     */     throws NamingException
/*     */   {
/* 597 */     return lookup(name, false);
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
/*     */   public Object lookupLink(String name)
/*     */     throws NamingException
/*     */   {
/* 613 */     return lookup(new CompositeName(name), false);
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
/*     */   public NameParser getNameParser(Name name)
/*     */     throws NamingException
/*     */   {
/* 635 */     while ((!name.isEmpty()) && (name.get(0).length() == 0))
/* 636 */       name = name.getSuffix(1);
/* 637 */     if (name.isEmpty()) {
/* 638 */       return nameParser;
/*     */     }
/* 640 */     if (name.size() > 1) {
/* 641 */       Object obj = this.bindings.get(name.get(0));
/* 642 */       if ((obj instanceof Context)) {
/* 643 */         return ((Context)obj).getNameParser(name.getSuffix(1));
/*     */       }
/*     */       
/* 646 */       throw new NotContextException(sm.getString("namingContext.contextExpected"));
/*     */     }
/*     */     
/*     */ 
/* 650 */     return nameParser;
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
/*     */   public NameParser getNameParser(String name)
/*     */     throws NamingException
/*     */   {
/* 666 */     return getNameParser(new CompositeName(name));
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
/*     */   public Name composeName(Name name, Name prefix)
/*     */     throws NamingException
/*     */   {
/* 687 */     prefix = (Name)prefix.clone();
/* 688 */     return prefix.addAll(name);
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
/*     */   public String composeName(String name, String prefix)
/*     */   {
/* 701 */     return prefix + "/" + name;
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
/*     */   public Object addToEnvironment(String propName, Object propVal)
/*     */   {
/* 715 */     return this.env.put(propName, propVal);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object removeFromEnvironment(String propName)
/*     */   {
/* 727 */     return this.env.remove(propName);
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
/*     */   public Hashtable<?, ?> getEnvironment()
/*     */   {
/* 742 */     return this.env;
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
/*     */   public void close()
/*     */     throws NamingException
/*     */   {
/* 758 */     if (!checkWritable()) {
/* 759 */       return;
/*     */     }
/* 761 */     this.env.clear();
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
/*     */   public String getNameInNamespace()
/*     */     throws NamingException
/*     */   {
/* 786 */     throw new OperationNotSupportedException(sm.getString("namingContext.noAbsoluteName"));
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
/*     */   protected Object lookup(Name name, boolean resolveLinks)
/*     */     throws NamingException
/*     */   {
/* 806 */     while ((!name.isEmpty()) && (name.get(0).length() == 0))
/* 807 */       name = name.getSuffix(1);
/* 808 */     if (name.isEmpty())
/*     */     {
/* 810 */       return new NamingContext(this.env, this.name, this.bindings);
/*     */     }
/*     */     
/* 813 */     NamingEntry entry = (NamingEntry)this.bindings.get(name.get(0));
/*     */     
/* 815 */     if (entry == null)
/*     */     {
/* 817 */       throw new NameNotFoundException(sm.getString("namingContext.nameNotBound", new Object[] { name, name.get(0) }));
/*     */     }
/*     */     
/* 820 */     if (name.size() > 1)
/*     */     {
/*     */ 
/* 823 */       if (entry.type != 10)
/*     */       {
/* 825 */         throw new NamingException(sm.getString("namingContext.contextExpected"));
/*     */       }
/* 827 */       return ((Context)entry.value).lookup(name.getSuffix(1));
/*     */     }
/* 829 */     if ((resolveLinks) && (entry.type == 1)) {
/* 830 */       String link = ((LinkRef)entry.value).getLinkName();
/* 831 */       if (link.startsWith("."))
/*     */       {
/* 833 */         return lookup(link.substring(1));
/*     */       }
/* 835 */       return new InitialContext(this.env).lookup(link);
/*     */     }
/* 837 */     if (entry.type == 2) {
/*     */       try
/*     */       {
/* 840 */         Object obj = NamingManager.getObjectInstance(entry.value, name, this, this.env);
/* 841 */         if ((entry.value instanceof ResourceRef)) {
/* 842 */           boolean singleton = Boolean.parseBoolean(
/*     */           
/* 844 */             (String)((ResourceRef)entry.value).get("singleton").getContent());
/* 845 */           if (singleton) {
/* 846 */             entry.type = 0;
/* 847 */             entry.value = obj;
/*     */           }
/*     */         }
/* 850 */         return obj;
/*     */       } catch (NamingException e) {
/* 852 */         throw e;
/*     */       } catch (Exception e) {
/* 854 */         log.warn(sm
/* 855 */           .getString("namingContext.failResolvingReference"), e);
/* 856 */         throw new NamingException(e.getMessage());
/*     */       }
/*     */     }
/* 859 */     return entry.value;
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
/*     */   protected void bind(Name name, Object obj, boolean rebind)
/*     */     throws NamingException
/*     */   {
/* 882 */     if (!checkWritable()) {
/* 883 */       return;
/*     */     }
/*     */     
/* 886 */     while ((!name.isEmpty()) && (name.get(0).length() == 0))
/* 887 */       name = name.getSuffix(1);
/* 888 */     if (name.isEmpty())
/*     */     {
/* 890 */       throw new NamingException(sm.getString("namingContext.invalidName"));
/*     */     }
/* 892 */     NamingEntry entry = (NamingEntry)this.bindings.get(name.get(0));
/*     */     
/* 894 */     if (name.size() > 1) {
/* 895 */       if (entry == null) {
/* 896 */         throw new NameNotFoundException(sm.getString("namingContext.nameNotBound", new Object[] { name, name
/* 897 */           .get(0) }));
/*     */       }
/* 899 */       if (entry.type == 10) {
/* 900 */         if (rebind) {
/* 901 */           ((Context)entry.value).rebind(name.getSuffix(1), obj);
/*     */         } else {
/* 903 */           ((Context)entry.value).bind(name.getSuffix(1), obj);
/*     */         }
/*     */       }
/*     */       else {
/* 907 */         throw new NamingException(sm.getString("namingContext.contextExpected"));
/*     */       }
/*     */     } else {
/* 910 */       if ((!rebind) && (entry != null))
/*     */       {
/* 912 */         throw new NameAlreadyBoundException(sm.getString("namingContext.alreadyBound", new Object[] {name.get(0) }));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 917 */       Object toBind = NamingManager.getStateToBind(obj, name, this, this.env);
/* 918 */       if ((toBind instanceof Context)) {
/* 919 */         entry = new NamingEntry(name.get(0), toBind, 10);
/*     */       }
/* 921 */       else if ((toBind instanceof LinkRef)) {
/* 922 */         entry = new NamingEntry(name.get(0), toBind, 1);
/*     */       }
/* 924 */       else if ((toBind instanceof Reference)) {
/* 925 */         entry = new NamingEntry(name.get(0), toBind, 2);
/*     */       }
/* 927 */       else if ((toBind instanceof Referenceable)) {
/* 928 */         toBind = ((Referenceable)toBind).getReference();
/* 929 */         entry = new NamingEntry(name.get(0), toBind, 2);
/*     */       }
/*     */       else {
/* 932 */         entry = new NamingEntry(name.get(0), toBind, 0);
/*     */       }
/*     */       
/* 935 */       this.bindings.put(name.get(0), entry);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isWritable()
/*     */   {
/* 946 */     return ContextAccessController.isWritable(this.name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean checkWritable()
/*     */     throws NamingException
/*     */   {
/* 957 */     if (isWritable()) {
/* 958 */       return true;
/*     */     }
/* 960 */     if (this.exceptionOnFailedWrite)
/*     */     {
/* 962 */       throw new OperationNotSupportedException(sm.getString("namingContext.readOnly"));
/*     */     }
/*     */     
/* 965 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\NamingContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */