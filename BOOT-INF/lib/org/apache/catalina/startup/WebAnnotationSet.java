/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.annotation.Resource;
/*     */ import javax.annotation.Resource.AuthenticationType;
/*     */ import javax.annotation.Resources;
/*     */ import javax.annotation.security.DeclareRoles;
/*     */ import javax.annotation.security.RunAs;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.catalina.deploy.NamingResourcesImpl;
/*     */ import org.apache.catalina.util.Introspection;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextEnvironment;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextResource;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextResourceEnvRef;
/*     */ import org.apache.tomcat.util.descriptor.web.ContextService;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterDef;
/*     */ import org.apache.tomcat.util.descriptor.web.MessageDestinationRef;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebAnnotationSet
/*     */ {
/*     */   private static final String SEPARATOR = "/";
/*  53 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.startup");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void loadApplicationAnnotations(Context context)
/*     */   {
/*  64 */     loadApplicationListenerAnnotations(context);
/*  65 */     loadApplicationFilterAnnotations(context);
/*  66 */     loadApplicationServletAnnotations(context);
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
/*     */   protected static void loadApplicationListenerAnnotations(Context context)
/*     */   {
/*  80 */     String[] applicationListeners = context.findApplicationListeners();
/*  81 */     for (String className : applicationListeners) {
/*  82 */       Class<?> classClass = Introspection.loadClass(context, className);
/*  83 */       if (classClass != null)
/*     */       {
/*     */ 
/*     */ 
/*  87 */         loadClassAnnotation(context, classClass);
/*  88 */         loadFieldsAnnotation(context, classClass);
/*  89 */         loadMethodsAnnotation(context, classClass);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void loadApplicationFilterAnnotations(Context context)
/*     */   {
/*  99 */     FilterDef[] filterDefs = context.findFilterDefs();
/* 100 */     for (FilterDef filterDef : filterDefs) {
/* 101 */       Class<?> classClass = Introspection.loadClass(context, filterDef
/* 102 */         .getFilterClass());
/* 103 */       if (classClass != null)
/*     */       {
/*     */ 
/*     */ 
/* 107 */         loadClassAnnotation(context, classClass);
/* 108 */         loadFieldsAnnotation(context, classClass);
/* 109 */         loadMethodsAnnotation(context, classClass);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void loadApplicationServletAnnotations(Context context)
/*     */   {
/* 120 */     Container[] children = context.findChildren();
/* 121 */     for (Container child : children) {
/* 122 */       if ((child instanceof Wrapper))
/*     */       {
/* 124 */         Wrapper wrapper = (Wrapper)child;
/* 125 */         if (wrapper.getServletClass() != null)
/*     */         {
/*     */ 
/*     */ 
/* 129 */           Class<?> classClass = Introspection.loadClass(context, wrapper
/* 130 */             .getServletClass());
/* 131 */           if (classClass != null)
/*     */           {
/*     */ 
/*     */ 
/* 135 */             loadClassAnnotation(context, classClass);
/* 136 */             loadFieldsAnnotation(context, classClass);
/* 137 */             loadMethodsAnnotation(context, classClass);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */             RunAs annotation = (RunAs)classClass.getAnnotation(RunAs.class);
/* 144 */             if (annotation != null) {
/* 145 */               wrapper.setRunAs(annotation.value());
/*     */             }
/*     */           }
/*     */         }
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
/*     */ 
/*     */   protected static void loadClassAnnotation(Context context, Class<?> classClass)
/*     */   {
/* 163 */     Resource resourceAnnotation = (Resource)classClass.getAnnotation(Resource.class);
/* 164 */     if (resourceAnnotation != null) {
/* 165 */       addResource(context, resourceAnnotation);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 170 */     Resources resourcesAnnotation = (Resources)classClass.getAnnotation(Resources.class);
/* 171 */     Resource localResource1; Resource resource; if ((resourcesAnnotation != null) && (resourcesAnnotation.value() != null)) {
/* 172 */       Resource[] arrayOfResource = resourcesAnnotation.value();int i = arrayOfResource.length; for (localResource1 = 0; localResource1 < i; localResource1++) { resource = arrayOfResource[localResource1];
/* 173 */         addResource(context, resource);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 250 */     DeclareRoles declareRolesAnnotation = (DeclareRoles)classClass.getAnnotation(DeclareRoles.class);
/* 251 */     if ((declareRolesAnnotation != null) && (declareRolesAnnotation.value() != null)) {
/* 252 */       String[] arrayOfString = declareRolesAnnotation.value();localResource1 = arrayOfString.length; for (resource = 0; resource < localResource1; resource++) { String role = arrayOfString[resource];
/* 253 */         context.addSecurityRole(role);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static void loadFieldsAnnotation(Context context, Class<?> classClass)
/*     */   {
/* 262 */     Field[] fields = Introspection.getDeclaredFields(classClass);
/* 263 */     if ((fields != null) && (fields.length > 0)) {
/* 264 */       for (Field field : fields) {
/* 265 */         Resource annotation = (Resource)field.getAnnotation(Resource.class);
/* 266 */         if (annotation != null) {
/* 267 */           String defaultName = classClass.getName() + "/" + field.getName();
/* 268 */           Class<?> defaultType = field.getType();
/* 269 */           addResource(context, annotation, defaultName, defaultType);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static void loadMethodsAnnotation(Context context, Class<?> classClass)
/*     */   {
/* 279 */     Method[] methods = Introspection.getDeclaredMethods(classClass);
/* 280 */     if ((methods != null) && (methods.length > 0)) {
/* 281 */       for (Method method : methods) {
/* 282 */         Resource annotation = (Resource)method.getAnnotation(Resource.class);
/* 283 */         if (annotation != null) {
/* 284 */           if (!Introspection.isValidSetter(method)) {
/* 285 */             throw new IllegalArgumentException(sm.getString("webAnnotationSet.invalidInjection"));
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 290 */           String defaultName = classClass.getName() + "/" + Introspection.getPropertyName(method);
/*     */           
/*     */ 
/* 293 */           Class<?> defaultType = method.getParameterTypes()[0];
/* 294 */           addResource(context, annotation, defaultName, defaultType);
/*     */         }
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
/*     */   protected static void addResource(Context context, Resource annotation)
/*     */   {
/* 309 */     addResource(context, annotation, null, null);
/*     */   }
/*     */   
/*     */   protected static void addResource(Context context, Resource annotation, String defaultName, Class<?> defaultType)
/*     */   {
/* 314 */     String name = getName(annotation, defaultName);
/* 315 */     String type = getType(annotation, defaultType);
/*     */     
/* 317 */     if ((type.equals("java.lang.String")) || 
/* 318 */       (type.equals("java.lang.Character")) || 
/* 319 */       (type.equals("java.lang.Integer")) || 
/* 320 */       (type.equals("java.lang.Boolean")) || 
/* 321 */       (type.equals("java.lang.Double")) || 
/* 322 */       (type.equals("java.lang.Byte")) || 
/* 323 */       (type.equals("java.lang.Short")) || 
/* 324 */       (type.equals("java.lang.Long")) || 
/* 325 */       (type.equals("java.lang.Float")))
/*     */     {
/*     */ 
/* 328 */       ContextEnvironment resource = new ContextEnvironment();
/*     */       
/* 330 */       resource.setName(name);
/* 331 */       resource.setType(type);
/*     */       
/* 333 */       resource.setDescription(annotation.description());
/*     */       
/* 335 */       resource.setValue(annotation.mappedName());
/*     */       
/* 337 */       context.getNamingResources().addEnvironment(resource);
/*     */     }
/* 339 */     else if (type.equals("javax.xml.rpc.Service"))
/*     */     {
/*     */ 
/* 342 */       ContextService service = new ContextService();
/*     */       
/* 344 */       service.setName(name);
/* 345 */       service.setWsdlfile(annotation.mappedName());
/*     */       
/* 347 */       service.setType(type);
/* 348 */       service.setDescription(annotation.description());
/*     */       
/* 350 */       context.getNamingResources().addService(service);
/*     */     }
/* 352 */     else if ((type.equals("javax.sql.DataSource")) || 
/* 353 */       (type.equals("javax.jms.ConnectionFactory")) || 
/* 354 */       (type.equals("javax.jms.QueueConnectionFactory")) || 
/* 355 */       (type.equals("javax.jms.TopicConnectionFactory")) || 
/* 356 */       (type.equals("javax.mail.Session")) || 
/* 357 */       (type.equals("java.net.URL")) || 
/* 358 */       (type.equals("javax.resource.cci.ConnectionFactory")) || 
/* 359 */       (type.equals("org.omg.CORBA_2_3.ORB")) || 
/* 360 */       (type.endsWith("ConnectionFactory")))
/*     */     {
/*     */ 
/* 363 */       ContextResource resource = new ContextResource();
/*     */       
/* 365 */       resource.setName(name);
/* 366 */       resource.setType(type);
/*     */       
/* 368 */       if (annotation.authenticationType() == Resource.AuthenticationType.CONTAINER)
/*     */       {
/* 370 */         resource.setAuth("Container");
/* 371 */       } else if (annotation.authenticationType() == Resource.AuthenticationType.APPLICATION)
/*     */       {
/* 373 */         resource.setAuth("Application");
/*     */       }
/*     */       
/* 376 */       resource.setScope(annotation.shareable() ? "Shareable" : "Unshareable");
/* 377 */       resource.setProperty("mappedName", annotation.mappedName());
/* 378 */       resource.setDescription(annotation.description());
/*     */       
/* 380 */       context.getNamingResources().addResource(resource);
/*     */     }
/* 382 */     else if ((type.equals("javax.jms.Queue")) || 
/* 383 */       (type.equals("javax.jms.Topic")))
/*     */     {
/*     */ 
/* 386 */       MessageDestinationRef resource = new MessageDestinationRef();
/*     */       
/* 388 */       resource.setName(name);
/* 389 */       resource.setType(type);
/*     */       
/* 391 */       resource.setUsage(annotation.mappedName());
/* 392 */       resource.setDescription(annotation.description());
/*     */       
/* 394 */       context.getNamingResources().addMessageDestinationRef(resource);
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 404 */       ContextResourceEnvRef resource = new ContextResourceEnvRef();
/*     */       
/* 406 */       resource.setName(name);
/* 407 */       resource.setType(type);
/*     */       
/* 409 */       resource.setProperty("mappedName", annotation.mappedName());
/* 410 */       resource.setDescription(annotation.description());
/*     */       
/* 412 */       context.getNamingResources().addResourceEnvRef(resource);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static String getType(Resource annotation, Class<?> defaultType)
/*     */   {
/* 419 */     Class<?> type = annotation.type();
/* 420 */     if (((type == null) || (type.equals(Object.class))) && 
/* 421 */       (defaultType != null)) {
/* 422 */       type = defaultType;
/*     */     }
/*     */     
/* 425 */     return Introspection.convertPrimitiveType(type).getCanonicalName();
/*     */   }
/*     */   
/*     */   private static String getName(Resource annotation, String defaultName)
/*     */   {
/* 430 */     String name = annotation.name();
/* 431 */     if (((name == null) || (name.equals(""))) && 
/* 432 */       (defaultName != null)) {
/* 433 */       name = defaultName;
/*     */     }
/*     */     
/* 436 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\WebAnnotationSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */