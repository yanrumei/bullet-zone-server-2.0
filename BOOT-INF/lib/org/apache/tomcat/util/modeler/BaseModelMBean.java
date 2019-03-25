/*     */ package org.apache.tomcat.util.modeler;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeChangeNotification;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.MBeanRegistration;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeErrorException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import javax.management.modelmbean.ModelMBeanNotificationBroadcaster;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BaseModelMBean
/*     */   implements DynamicMBean, MBeanRegistration, ModelMBeanNotificationBroadcaster
/*     */ {
/* 104 */   private static final Log log = LogFactory.getLog(BaseModelMBean.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */   protected ObjectName oname = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 128 */   protected BaseNotificationBroadcaster attributeBroadcaster = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 133 */   protected BaseNotificationBroadcaster generalBroadcaster = null;
/*     */   
/*     */ 
/*     */ 
/* 137 */   protected ManagedBean managedBean = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 142 */   protected Object resource = null;
/*     */   
/*     */ 
/*     */ 
/* 146 */   static final Object[] NO_ARGS_PARAM = new Object[0];
/*     */   
/* 148 */   protected String resourceType = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BaseModelMBean()
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getAttribute(String name)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/* 170 */     if (name == null) {
/* 171 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name is null"), "Attribute name is null");
/*     */     }
/*     */     
/*     */ 
/* 175 */     if (((this.resource instanceof DynamicMBean)) && (!(this.resource instanceof BaseModelMBean)))
/*     */     {
/* 177 */       return ((DynamicMBean)this.resource).getAttribute(name);
/*     */     }
/*     */     
/* 180 */     Method m = this.managedBean.getGetter(name, this, this.resource);
/* 181 */     Object result = null;
/*     */     try {
/* 183 */       Class<?> declaring = m.getDeclaringClass();
/*     */       
/*     */ 
/* 186 */       if (declaring.isAssignableFrom(getClass())) {
/* 187 */         result = m.invoke(this, NO_ARGS_PARAM);
/*     */       } else {
/* 189 */         result = m.invoke(this.resource, NO_ARGS_PARAM);
/*     */       }
/*     */     } catch (InvocationTargetException e) {
/* 192 */       Throwable t = e.getTargetException();
/* 193 */       if (t == null)
/* 194 */         t = e;
/* 195 */       if ((t instanceof RuntimeException)) {
/* 196 */         throw new RuntimeOperationsException((RuntimeException)t, "Exception invoking method " + name);
/*     */       }
/* 198 */       if ((t instanceof Error)) {
/* 199 */         throw new RuntimeErrorException((Error)t, "Error invoking method " + name);
/*     */       }
/*     */       
/* 202 */       throw new MBeanException(e, "Exception invoking method " + name);
/*     */     }
/*     */     catch (Exception e) {
/* 205 */       throw new MBeanException(e, "Exception invoking method " + name);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 211 */     return result;
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
/*     */   public AttributeList getAttributes(String[] names)
/*     */   {
/* 224 */     if (names == null) {
/* 225 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute names list is null"), "Attribute names list is null");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 230 */     AttributeList response = new AttributeList();
/* 231 */     for (int i = 0; i < names.length; i++) {
/*     */       try {
/* 233 */         response.add(new Attribute(names[i], getAttribute(names[i])));
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/* 239 */     return response;
/*     */   }
/*     */   
/*     */   public void setManagedBean(ManagedBean managedBean)
/*     */   {
/* 244 */     this.managedBean = managedBean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MBeanInfo getMBeanInfo()
/*     */   {
/* 252 */     return this.managedBean.getMBeanInfo();
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
/*     */   public Object invoke(String name, Object[] params, String[] signature)
/*     */     throws MBeanException, ReflectionException
/*     */   {
/* 279 */     if (((this.resource instanceof DynamicMBean)) && (!(this.resource instanceof BaseModelMBean)))
/*     */     {
/* 281 */       return ((DynamicMBean)this.resource).invoke(name, params, signature);
/*     */     }
/*     */     
/*     */ 
/* 285 */     if (name == null) {
/* 286 */       throw new RuntimeOperationsException(new IllegalArgumentException("Method name is null"), "Method name is null");
/*     */     }
/*     */     
/*     */ 
/* 290 */     if (log.isDebugEnabled()) { log.debug("Invoke " + name);
/*     */     }
/* 292 */     Method method = this.managedBean.getInvoke(name, params, signature, this, this.resource);
/*     */     
/*     */ 
/* 295 */     Object result = null;
/*     */     try {
/* 297 */       if (method.getDeclaringClass().isAssignableFrom(getClass())) {
/* 298 */         result = method.invoke(this, params);
/*     */       } else {
/* 300 */         result = method.invoke(this.resource, params);
/*     */       }
/*     */     } catch (InvocationTargetException e) {
/* 303 */       Throwable t = e.getTargetException();
/* 304 */       log.error("Exception invoking method " + name, t);
/* 305 */       if (t == null)
/* 306 */         t = e;
/* 307 */       if ((t instanceof RuntimeException)) {
/* 308 */         throw new RuntimeOperationsException((RuntimeException)t, "Exception invoking method " + name);
/*     */       }
/* 310 */       if ((t instanceof Error)) {
/* 311 */         throw new RuntimeErrorException((Error)t, "Error invoking method " + name);
/*     */       }
/*     */       
/* 314 */       throw new MBeanException((Exception)t, "Exception invoking method " + name);
/*     */     }
/*     */     catch (Exception e) {
/* 317 */       log.error("Exception invoking method " + name, e);
/* 318 */       throw new MBeanException(e, "Exception invoking method " + name);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 324 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   static Class<?> getAttributeClass(String signature)
/*     */     throws ReflectionException
/*     */   {
/* 331 */     if (signature.equals(Boolean.TYPE.getName()))
/* 332 */       return Boolean.TYPE;
/* 333 */     if (signature.equals(Byte.TYPE.getName()))
/* 334 */       return Byte.TYPE;
/* 335 */     if (signature.equals(Character.TYPE.getName()))
/* 336 */       return Character.TYPE;
/* 337 */     if (signature.equals(Double.TYPE.getName()))
/* 338 */       return Double.TYPE;
/* 339 */     if (signature.equals(Float.TYPE.getName()))
/* 340 */       return Float.TYPE;
/* 341 */     if (signature.equals(Integer.TYPE.getName()))
/* 342 */       return Integer.TYPE;
/* 343 */     if (signature.equals(Long.TYPE.getName()))
/* 344 */       return Long.TYPE;
/* 345 */     if (signature.equals(Short.TYPE.getName())) {
/* 346 */       return Short.TYPE;
/*     */     }
/*     */     try {
/* 349 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 350 */       if (cl != null) {
/* 351 */         return cl.loadClass(signature);
/*     */       }
/*     */     } catch (ClassNotFoundException localClassNotFoundException1) {}
/*     */     try {
/* 355 */       return Class.forName(signature);
/*     */     } catch (ClassNotFoundException e) {
/* 357 */       throw new ReflectionException(e, "Cannot find Class for " + signature);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAttribute(Attribute attribute)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/* 381 */     if (log.isDebugEnabled()) {
/* 382 */       log.debug("Setting attribute " + this + " " + attribute);
/*     */     }
/* 384 */     if (((this.resource instanceof DynamicMBean)) && (!(this.resource instanceof BaseModelMBean)))
/*     */     {
/*     */       try {
/* 387 */         ((DynamicMBean)this.resource).setAttribute(attribute);
/*     */       } catch (InvalidAttributeValueException e) {
/* 389 */         throw new MBeanException(e);
/*     */       }
/* 391 */       return;
/*     */     }
/*     */     
/*     */ 
/* 395 */     if (attribute == null) {
/* 396 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute is null"), "Attribute is null");
/*     */     }
/*     */     
/*     */ 
/* 400 */     String name = attribute.getName();
/* 401 */     Object value = attribute.getValue();
/*     */     
/* 403 */     if (name == null) {
/* 404 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name is null"), "Attribute name is null");
/*     */     }
/*     */     
/*     */ 
/* 408 */     Object oldValue = null;
/*     */     
/*     */ 
/*     */ 
/* 412 */     Method m = this.managedBean.getSetter(name, this, this.resource);
/*     */     try
/*     */     {
/* 415 */       if (m.getDeclaringClass().isAssignableFrom(getClass())) {
/* 416 */         m.invoke(this, new Object[] { value });
/*     */       } else {
/* 418 */         m.invoke(this.resource, new Object[] { value });
/*     */       }
/*     */     } catch (InvocationTargetException e) {
/* 421 */       Throwable t = e.getTargetException();
/* 422 */       if (t == null)
/* 423 */         t = e;
/* 424 */       if ((t instanceof RuntimeException)) {
/* 425 */         throw new RuntimeOperationsException((RuntimeException)t, "Exception invoking method " + name);
/*     */       }
/* 427 */       if ((t instanceof Error)) {
/* 428 */         throw new RuntimeErrorException((Error)t, "Error invoking method " + name);
/*     */       }
/*     */       
/* 431 */       throw new MBeanException(e, "Exception invoking method " + name);
/*     */     }
/*     */     catch (Exception e) {
/* 434 */       log.error("Exception invoking method " + name, e);
/* 435 */       throw new MBeanException(e, "Exception invoking method " + name);
/*     */     }
/*     */     try
/*     */     {
/* 439 */       sendAttributeChangeNotification(new Attribute(name, oldValue), attribute);
/*     */     }
/*     */     catch (Exception ex) {
/* 442 */       log.error("Error sending notification " + name, ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 453 */     if (this.resource == null)
/* 454 */       return "BaseModelMbean[" + this.resourceType + "]";
/* 455 */     return this.resource.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AttributeList setAttributes(AttributeList attributes)
/*     */   {
/* 467 */     AttributeList response = new AttributeList();
/*     */     
/*     */ 
/* 470 */     if (attributes == null) {
/* 471 */       return response;
/*     */     }
/*     */     
/* 474 */     String[] names = new String[attributes.size()];
/* 475 */     int n = 0;
/* 476 */     Iterator<?> items = attributes.iterator();
/* 477 */     while (items.hasNext()) {
/* 478 */       Attribute item = (Attribute)items.next();
/* 479 */       names[(n++)] = item.getName();
/*     */       try {
/* 481 */         setAttribute(item);
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/* 487 */     return getAttributes(names);
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
/*     */   public Object getManagedResource()
/*     */     throws InstanceNotFoundException, InvalidTargetObjectTypeException, MBeanException, RuntimeOperationsException
/*     */   {
/* 513 */     if (this.resource == null) {
/* 514 */       throw new RuntimeOperationsException(new IllegalArgumentException("Managed resource is null"), "Managed resource is null");
/*     */     }
/*     */     
/*     */ 
/* 518 */     return this.resource;
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
/*     */   public void setManagedResource(Object resource, String type)
/*     */     throws InstanceNotFoundException, MBeanException, RuntimeOperationsException
/*     */   {
/* 546 */     if (resource == null) {
/* 547 */       throw new RuntimeOperationsException(new IllegalArgumentException("Managed resource is null"), "Managed resource is null");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 554 */     this.resource = resource;
/* 555 */     this.resourceType = resource.getClass().getName();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAttributeChangeNotificationListener(NotificationListener listener, String name, Object handback)
/*     */     throws IllegalArgumentException
/*     */   {
/* 591 */     if (listener == null)
/* 592 */       throw new IllegalArgumentException("Listener is null");
/* 593 */     if (this.attributeBroadcaster == null) {
/* 594 */       this.attributeBroadcaster = new BaseNotificationBroadcaster();
/*     */     }
/* 596 */     if (log.isDebugEnabled()) {
/* 597 */       log.debug("addAttributeNotificationListener " + listener);
/*     */     }
/* 599 */     BaseAttributeFilter filter = new BaseAttributeFilter(name);
/* 600 */     this.attributeBroadcaster
/* 601 */       .addNotificationListener(listener, filter, handback);
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
/*     */   public void removeAttributeChangeNotificationListener(NotificationListener listener, String name)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 622 */     if (listener == null) {
/* 623 */       throw new IllegalArgumentException("Listener is null");
/*     */     }
/*     */     
/* 626 */     if (this.attributeBroadcaster != null) {
/* 627 */       this.attributeBroadcaster.removeNotificationListener(listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendAttributeChangeNotification(AttributeChangeNotification notification)
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {
/* 650 */     if (notification == null) {
/* 651 */       throw new RuntimeOperationsException(new IllegalArgumentException("Notification is null"), "Notification is null");
/*     */     }
/*     */     
/* 654 */     if (this.attributeBroadcaster == null)
/* 655 */       return;
/* 656 */     if (log.isDebugEnabled())
/* 657 */       log.debug("AttributeChangeNotification " + notification);
/* 658 */     this.attributeBroadcaster.sendNotification(notification);
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
/*     */   public void sendAttributeChangeNotification(Attribute oldValue, Attribute newValue)
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {
/* 681 */     String type = null;
/* 682 */     if (newValue.getValue() != null) {
/* 683 */       type = newValue.getValue().getClass().getName();
/* 684 */     } else if (oldValue.getValue() != null) {
/* 685 */       type = oldValue.getValue().getClass().getName();
/*     */     } else {
/* 687 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 694 */     AttributeChangeNotification notification = new AttributeChangeNotification(this, 1L, System.currentTimeMillis(), "Attribute value has changed", oldValue.getName(), type, oldValue.getValue(), newValue.getValue());
/* 695 */     sendAttributeChangeNotification(notification);
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
/*     */   public void sendNotification(Notification notification)
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {
/* 715 */     if (notification == null) {
/* 716 */       throw new RuntimeOperationsException(new IllegalArgumentException("Notification is null"), "Notification is null");
/*     */     }
/*     */     
/* 719 */     if (this.generalBroadcaster == null)
/* 720 */       return;
/* 721 */     this.generalBroadcaster.sendNotification(notification);
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
/*     */   public void sendNotification(String message)
/*     */     throws MBeanException, RuntimeOperationsException
/*     */   {
/* 741 */     if (message == null) {
/* 742 */       throw new RuntimeOperationsException(new IllegalArgumentException("Message is null"), "Message is null");
/*     */     }
/*     */     
/* 745 */     Notification notification = new Notification("jmx.modelmbean.generic", this, 1L, message);
/*     */     
/* 747 */     sendNotification(notification);
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
/*     */   public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
/*     */     throws IllegalArgumentException
/*     */   {
/* 772 */     if (listener == null) {
/* 773 */       throw new IllegalArgumentException("Listener is null");
/*     */     }
/* 775 */     if (log.isDebugEnabled()) { log.debug("addNotificationListener " + listener);
/*     */     }
/* 777 */     if (this.generalBroadcaster == null) {
/* 778 */       this.generalBroadcaster = new BaseNotificationBroadcaster();
/*     */     }
/* 780 */     this.generalBroadcaster.addNotificationListener(listener, filter, handback);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 786 */     if (this.attributeBroadcaster == null) {
/* 787 */       this.attributeBroadcaster = new BaseNotificationBroadcaster();
/*     */     }
/* 789 */     if (log.isDebugEnabled()) {
/* 790 */       log.debug("addAttributeNotificationListener " + listener);
/*     */     }
/*     */     
/* 793 */     this.attributeBroadcaster.addNotificationListener(listener, filter, handback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 805 */     MBeanNotificationInfo[] current = getMBeanInfo().getNotifications();
/* 806 */     MBeanNotificationInfo[] response = new MBeanNotificationInfo[current.length + 2];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 817 */     response[0] = new MBeanNotificationInfo(new String[] { "jmx.modelmbean.generic" }, "GENERIC", "Text message notification from the managed resource");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 830 */     response[1] = new MBeanNotificationInfo(new String[] { "jmx.attribute.change" }, "ATTRIBUTE_CHANGE", "Observed MBean attribute value has changed");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 837 */     System.arraycopy(current, 0, response, 2, current.length);
/* 838 */     return response;
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
/*     */   public void removeNotificationListener(NotificationListener listener)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 856 */     if (listener == null) {
/* 857 */       throw new IllegalArgumentException("Listener is null");
/*     */     }
/* 859 */     if (this.generalBroadcaster != null) {
/* 860 */       this.generalBroadcaster.removeNotificationListener(listener);
/*     */     }
/*     */     
/* 863 */     if (this.attributeBroadcaster != null) {
/* 864 */       this.attributeBroadcaster.removeNotificationListener(listener);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getModelerType()
/*     */   {
/* 870 */     return this.resourceType;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/* 874 */     return getModelerType();
/*     */   }
/*     */   
/*     */   public ObjectName getJmxName() {
/* 878 */     return this.oname;
/*     */   }
/*     */   
/*     */   public String getObjectName() {
/* 882 */     if (this.oname != null) {
/* 883 */       return this.oname.toString();
/*     */     }
/* 885 */     return null;
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
/*     */   public ObjectName preRegister(MBeanServer server, ObjectName name)
/*     */     throws Exception
/*     */   {
/* 899 */     if (log.isDebugEnabled())
/* 900 */       log.debug("preRegister " + this.resource + " " + name);
/* 901 */     this.oname = name;
/* 902 */     if ((this.resource instanceof MBeanRegistration)) {
/* 903 */       this.oname = ((MBeanRegistration)this.resource).preRegister(server, name);
/*     */     }
/* 905 */     return this.oname;
/*     */   }
/*     */   
/*     */   public void postRegister(Boolean registrationDone)
/*     */   {
/* 910 */     if ((this.resource instanceof MBeanRegistration)) {
/* 911 */       ((MBeanRegistration)this.resource).postRegister(registrationDone);
/*     */     }
/*     */   }
/*     */   
/*     */   public void preDeregister() throws Exception
/*     */   {
/* 917 */     if ((this.resource instanceof MBeanRegistration)) {
/* 918 */       ((MBeanRegistration)this.resource).preDeregister();
/*     */     }
/*     */   }
/*     */   
/*     */   public void postDeregister()
/*     */   {
/* 924 */     if ((this.resource instanceof MBeanRegistration)) {
/* 925 */       ((MBeanRegistration)this.resource).postDeregister();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\BaseModelMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */