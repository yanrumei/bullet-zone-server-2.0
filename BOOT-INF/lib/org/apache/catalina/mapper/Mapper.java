/*      */ package org.apache.catalina.mapper;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.WebResource;
/*      */ import org.apache.catalina.WebResourceRoot;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.servlet4preview.http.MappingMatch;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.buf.Ascii;
/*      */ import org.apache.tomcat.util.buf.CharChunk;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Mapper
/*      */ {
/*   50 */   private static final Log log = LogFactory.getLog(Mapper.class);
/*      */   
/*   52 */   private static final StringManager sm = StringManager.getManager(Mapper.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   61 */   volatile MappedHost[] hosts = new MappedHost[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   67 */   private String defaultHostName = null;
/*   68 */   private volatile MappedHost defaultHost = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   75 */   private final Map<Context, ContextVersion> contextObjectToContextVersionMap = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void setDefaultHostName(String defaultHostName)
/*      */   {
/*   87 */     this.defaultHostName = renameWildcardHost(defaultHostName);
/*   88 */     if (this.defaultHostName == null) {
/*   89 */       this.defaultHost = null;
/*      */     } else {
/*   91 */       this.defaultHost = ((MappedHost)exactFind(this.hosts, this.defaultHostName));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void addHost(String name, String[] aliases, Host host)
/*      */   {
/*  105 */     name = renameWildcardHost(name);
/*  106 */     MappedHost[] newHosts = new MappedHost[this.hosts.length + 1];
/*  107 */     MappedHost newHost = new MappedHost(name, host);
/*  108 */     if (insertMap(this.hosts, newHosts, newHost)) {
/*  109 */       this.hosts = newHosts;
/*  110 */       if (newHost.name.equals(this.defaultHostName)) {
/*  111 */         this.defaultHost = newHost;
/*      */       }
/*  113 */       if (log.isDebugEnabled()) {
/*  114 */         log.debug(sm.getString("mapper.addHost.success", new Object[] { name }));
/*      */       }
/*      */     } else {
/*  117 */       MappedHost duplicate = this.hosts[find(this.hosts, name)];
/*  118 */       if (duplicate.object == host)
/*      */       {
/*      */ 
/*  121 */         if (log.isDebugEnabled()) {
/*  122 */           log.debug(sm.getString("mapper.addHost.sameHost", new Object[] { name }));
/*      */         }
/*  124 */         newHost = duplicate;
/*      */       } else {
/*  126 */         log.error(sm.getString("mapper.duplicateHost", new Object[] { name, duplicate
/*  127 */           .getRealHostName() }));
/*      */         
/*      */ 
/*  130 */         return;
/*      */       }
/*      */     }
/*  133 */     List<MappedHost> newAliases = new ArrayList(aliases.length);
/*  134 */     for (String alias : aliases) {
/*  135 */       alias = renameWildcardHost(alias);
/*  136 */       MappedHost newAlias = new MappedHost(alias, newHost);
/*  137 */       if (addHostAliasImpl(newAlias)) {
/*  138 */         newAliases.add(newAlias);
/*      */       }
/*      */     }
/*  141 */     newHost.addAliases(newAliases);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void removeHost(String name)
/*      */   {
/*  151 */     name = renameWildcardHost(name);
/*      */     
/*  153 */     MappedHost host = (MappedHost)exactFind(this.hosts, name);
/*  154 */     if ((host == null) || (host.isAlias())) {
/*  155 */       return;
/*      */     }
/*  157 */     MappedHost[] newHosts = (MappedHost[])this.hosts.clone();
/*      */     
/*  159 */     int j = 0;
/*  160 */     for (int i = 0; i < newHosts.length; i++) {
/*  161 */       if (newHosts[i].getRealHost() != host) {
/*  162 */         newHosts[(j++)] = newHosts[i];
/*      */       }
/*      */     }
/*  165 */     this.hosts = ((MappedHost[])Arrays.copyOf(newHosts, j));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void addHostAlias(String name, String alias)
/*      */   {
/*  174 */     MappedHost realHost = (MappedHost)exactFind(this.hosts, name);
/*  175 */     if (realHost == null)
/*      */     {
/*      */ 
/*  178 */       return;
/*      */     }
/*  180 */     alias = renameWildcardHost(alias);
/*  181 */     MappedHost newAlias = new MappedHost(alias, realHost);
/*  182 */     if (addHostAliasImpl(newAlias)) {
/*  183 */       realHost.addAlias(newAlias);
/*      */     }
/*      */   }
/*      */   
/*      */   private synchronized boolean addHostAliasImpl(MappedHost newAlias) {
/*  188 */     MappedHost[] newHosts = new MappedHost[this.hosts.length + 1];
/*  189 */     if (insertMap(this.hosts, newHosts, newAlias)) {
/*  190 */       this.hosts = newHosts;
/*  191 */       if (newAlias.name.equals(this.defaultHostName)) {
/*  192 */         this.defaultHost = newAlias;
/*      */       }
/*  194 */       if (log.isDebugEnabled()) {
/*  195 */         log.debug(sm.getString("mapper.addHostAlias.success", new Object[] { newAlias.name, newAlias
/*  196 */           .getRealHostName() }));
/*      */       }
/*  198 */       return true;
/*      */     }
/*  200 */     MappedHost duplicate = this.hosts[find(this.hosts, newAlias.name)];
/*  201 */     if (duplicate.getRealHost() == newAlias.getRealHost())
/*      */     {
/*      */ 
/*      */ 
/*  205 */       if (log.isDebugEnabled()) {
/*  206 */         log.debug(sm.getString("mapper.addHostAlias.sameHost", new Object[] { newAlias.name, newAlias
/*  207 */           .getRealHostName() }));
/*      */       }
/*  209 */       return false;
/*      */     }
/*  211 */     log.error(sm.getString("mapper.duplicateHostAlias", new Object[] { newAlias.name, newAlias
/*  212 */       .getRealHostName(), duplicate.getRealHostName() }));
/*  213 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void removeHostAlias(String alias)
/*      */   {
/*  222 */     alias = renameWildcardHost(alias);
/*      */     
/*  224 */     MappedHost hostMapping = (MappedHost)exactFind(this.hosts, alias);
/*  225 */     if ((hostMapping == null) || (!hostMapping.isAlias())) {
/*  226 */       return;
/*      */     }
/*  228 */     MappedHost[] newHosts = new MappedHost[this.hosts.length - 1];
/*  229 */     if (removeMap(this.hosts, newHosts, alias)) {
/*  230 */       this.hosts = newHosts;
/*  231 */       hostMapping.getRealHost().removeAlias(hostMapping);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateContextList(MappedHost realHost, ContextList newContextList)
/*      */   {
/*  243 */     realHost.contextList = newContextList;
/*  244 */     for (MappedHost alias : realHost.getAliases()) {
/*  245 */       alias.contextList = newContextList;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addContextVersion(String hostName, Host host, String path, String version, Context context, String[] welcomeResources, WebResourceRoot resources, Collection<WrapperMappingInfo> wrappers)
/*      */   {
/*  265 */     hostName = renameWildcardHost(hostName);
/*      */     
/*  267 */     MappedHost mappedHost = (MappedHost)exactFind(this.hosts, hostName);
/*  268 */     if (mappedHost == null) {
/*  269 */       addHost(hostName, new String[0], host);
/*  270 */       mappedHost = (MappedHost)exactFind(this.hosts, hostName);
/*  271 */       if (mappedHost == null) {
/*  272 */         log.error("No host found: " + hostName);
/*  273 */         return;
/*      */       }
/*      */     }
/*  276 */     if (mappedHost.isAlias()) {
/*  277 */       log.error("No host found: " + hostName);
/*  278 */       return;
/*      */     }
/*  280 */     int slashCount = slashCount(path);
/*  281 */     synchronized (mappedHost) {
/*  282 */       ContextVersion newContextVersion = new ContextVersion(version, path, slashCount, context, resources, welcomeResources);
/*      */       
/*  284 */       if (wrappers != null) {
/*  285 */         addWrappers(newContextVersion, wrappers);
/*      */       }
/*      */       
/*  288 */       ContextList contextList = mappedHost.contextList;
/*  289 */       MappedContext mappedContext = (MappedContext)exactFind(contextList.contexts, path);
/*  290 */       if (mappedContext == null) {
/*  291 */         mappedContext = new MappedContext(path, newContextVersion);
/*  292 */         ContextList newContextList = contextList.addContext(mappedContext, slashCount);
/*      */         
/*  294 */         if (newContextList != null) {
/*  295 */           updateContextList(mappedHost, newContextList);
/*  296 */           this.contextObjectToContextVersionMap.put(context, newContextVersion);
/*      */         }
/*      */       } else {
/*  299 */         ContextVersion[] contextVersions = mappedContext.versions;
/*  300 */         ContextVersion[] newContextVersions = new ContextVersion[contextVersions.length + 1];
/*  301 */         if (insertMap(contextVersions, newContextVersions, newContextVersion))
/*      */         {
/*  303 */           mappedContext.versions = newContextVersions;
/*  304 */           this.contextObjectToContextVersionMap.put(context, newContextVersion);
/*      */         }
/*      */         else
/*      */         {
/*  308 */           int pos = find(contextVersions, version);
/*  309 */           if ((pos >= 0) && (contextVersions[pos].name.equals(version))) {
/*  310 */             contextVersions[pos] = newContextVersion;
/*  311 */             this.contextObjectToContextVersionMap.put(context, newContextVersion);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeContextVersion(Context ctxt, String hostName, String path, String version)
/*      */   {
/*  331 */     hostName = renameWildcardHost(hostName);
/*  332 */     this.contextObjectToContextVersionMap.remove(ctxt);
/*      */     
/*  334 */     MappedHost host = (MappedHost)exactFind(this.hosts, hostName);
/*  335 */     if ((host == null) || (host.isAlias())) {
/*  336 */       return;
/*      */     }
/*      */     
/*  339 */     synchronized (host) {
/*  340 */       ContextList contextList = host.contextList;
/*  341 */       MappedContext context = (MappedContext)exactFind(contextList.contexts, path);
/*  342 */       if (context == null) {
/*  343 */         return;
/*      */       }
/*      */       
/*  346 */       ContextVersion[] contextVersions = context.versions;
/*  347 */       ContextVersion[] newContextVersions = new ContextVersion[contextVersions.length - 1];
/*      */       
/*  349 */       if (removeMap(contextVersions, newContextVersions, version)) {
/*  350 */         if (newContextVersions.length == 0)
/*      */         {
/*  352 */           ContextList newContextList = contextList.removeContext(path);
/*  353 */           if (newContextList != null) {
/*  354 */             updateContextList(host, newContextList);
/*      */           }
/*      */         } else {
/*  357 */           context.versions = newContextVersions;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void pauseContextVersion(Context ctxt, String hostName, String contextPath, String version)
/*      */   {
/*  375 */     hostName = renameWildcardHost(hostName);
/*  376 */     ContextVersion contextVersion = findContextVersion(hostName, contextPath, version, true);
/*      */     
/*  378 */     if ((contextVersion == null) || (!ctxt.equals(contextVersion.object))) {
/*  379 */       return;
/*      */     }
/*  381 */     contextVersion.markPaused();
/*      */   }
/*      */   
/*      */ 
/*      */   private ContextVersion findContextVersion(String hostName, String contextPath, String version, boolean silent)
/*      */   {
/*  387 */     MappedHost host = (MappedHost)exactFind(this.hosts, hostName);
/*  388 */     if ((host == null) || (host.isAlias())) {
/*  389 */       if (!silent) {
/*  390 */         log.error("No host found: " + hostName);
/*      */       }
/*  392 */       return null;
/*      */     }
/*  394 */     MappedContext context = (MappedContext)exactFind(host.contextList.contexts, contextPath);
/*      */     
/*  396 */     if (context == null) {
/*  397 */       if (!silent) {
/*  398 */         log.error("No context found: " + contextPath);
/*      */       }
/*  400 */       return null;
/*      */     }
/*  402 */     ContextVersion contextVersion = (ContextVersion)exactFind(context.versions, version);
/*  403 */     if (contextVersion == null) {
/*  404 */       if (!silent) {
/*  405 */         log.error("No context version found: " + contextPath + " " + version);
/*      */       }
/*      */       
/*  408 */       return null;
/*      */     }
/*  410 */     return contextVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void addWrapper(String hostName, String contextPath, String version, String path, Wrapper wrapper, boolean jspWildCard, boolean resourceOnly)
/*      */   {
/*  417 */     hostName = renameWildcardHost(hostName);
/*  418 */     ContextVersion contextVersion = findContextVersion(hostName, contextPath, version, false);
/*      */     
/*  420 */     if (contextVersion == null) {
/*  421 */       return;
/*      */     }
/*  423 */     addWrapper(contextVersion, path, wrapper, jspWildCard, resourceOnly);
/*      */   }
/*      */   
/*      */   public void addWrappers(String hostName, String contextPath, String version, Collection<WrapperMappingInfo> wrappers)
/*      */   {
/*  428 */     hostName = renameWildcardHost(hostName);
/*  429 */     ContextVersion contextVersion = findContextVersion(hostName, contextPath, version, false);
/*      */     
/*  431 */     if (contextVersion == null) {
/*  432 */       return;
/*      */     }
/*  434 */     addWrappers(contextVersion, wrappers);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void addWrappers(ContextVersion contextVersion, Collection<WrapperMappingInfo> wrappers)
/*      */   {
/*  445 */     for (WrapperMappingInfo wrapper : wrappers) {
/*  446 */       addWrapper(contextVersion, wrapper.getMapping(), wrapper
/*  447 */         .getWrapper(), wrapper.isJspWildCard(), wrapper
/*  448 */         .isResourceOnly());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void addWrapper(ContextVersion context, String path, Wrapper wrapper, boolean jspWildCard, boolean resourceOnly)
/*      */   {
/*  466 */     synchronized (context) {
/*  467 */       if (path.endsWith("/*"))
/*      */       {
/*  469 */         String name = path.substring(0, path.length() - 2);
/*  470 */         MappedWrapper newWrapper = new MappedWrapper(name, wrapper, jspWildCard, resourceOnly);
/*      */         
/*  472 */         MappedWrapper[] oldWrappers = context.wildcardWrappers;
/*  473 */         MappedWrapper[] newWrappers = new MappedWrapper[oldWrappers.length + 1];
/*  474 */         if (insertMap(oldWrappers, newWrappers, newWrapper)) {
/*  475 */           context.wildcardWrappers = newWrappers;
/*  476 */           int slashCount = slashCount(newWrapper.name);
/*  477 */           if (slashCount > context.nesting) {
/*  478 */             context.nesting = slashCount;
/*      */           }
/*      */         }
/*  481 */       } else if (path.startsWith("*."))
/*      */       {
/*  483 */         String name = path.substring(2);
/*  484 */         MappedWrapper newWrapper = new MappedWrapper(name, wrapper, jspWildCard, resourceOnly);
/*      */         
/*  486 */         MappedWrapper[] oldWrappers = context.extensionWrappers;
/*  487 */         MappedWrapper[] newWrappers = new MappedWrapper[oldWrappers.length + 1];
/*      */         
/*  489 */         if (insertMap(oldWrappers, newWrappers, newWrapper)) {
/*  490 */           context.extensionWrappers = newWrappers;
/*      */         }
/*  492 */       } else if (path.equals("/"))
/*      */       {
/*  494 */         MappedWrapper newWrapper = new MappedWrapper("", wrapper, jspWildCard, resourceOnly);
/*      */         
/*  496 */         context.defaultWrapper = newWrapper;
/*      */       } else {
/*      */         String name;
/*      */         String name;
/*  500 */         if (path.length() == 0)
/*      */         {
/*      */ 
/*  503 */           name = "/";
/*      */         } else {
/*  505 */           name = path;
/*      */         }
/*  507 */         MappedWrapper newWrapper = new MappedWrapper(name, wrapper, jspWildCard, resourceOnly);
/*      */         
/*  509 */         MappedWrapper[] oldWrappers = context.exactWrappers;
/*  510 */         MappedWrapper[] newWrappers = new MappedWrapper[oldWrappers.length + 1];
/*  511 */         if (insertMap(oldWrappers, newWrappers, newWrapper)) {
/*  512 */           context.exactWrappers = newWrappers;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeWrapper(String hostName, String contextPath, String version, String path)
/*      */   {
/*  529 */     hostName = renameWildcardHost(hostName);
/*  530 */     ContextVersion contextVersion = findContextVersion(hostName, contextPath, version, true);
/*      */     
/*  532 */     if ((contextVersion == null) || (contextVersion.isPaused())) {
/*  533 */       return;
/*      */     }
/*  535 */     removeWrapper(contextVersion, path);
/*      */   }
/*      */   
/*      */   protected void removeWrapper(ContextVersion context, String path)
/*      */   {
/*  540 */     if (log.isDebugEnabled()) {
/*  541 */       log.debug(sm.getString("mapper.removeWrapper", new Object[] { context.name, path }));
/*      */     }
/*      */     
/*  544 */     synchronized (context) {
/*  545 */       if (path.endsWith("/*"))
/*      */       {
/*  547 */         String name = path.substring(0, path.length() - 2);
/*  548 */         MappedWrapper[] oldWrappers = context.wildcardWrappers;
/*  549 */         if (oldWrappers.length == 0) {
/*  550 */           return;
/*      */         }
/*  552 */         MappedWrapper[] newWrappers = new MappedWrapper[oldWrappers.length - 1];
/*      */         
/*  554 */         if (removeMap(oldWrappers, newWrappers, name))
/*      */         {
/*  556 */           context.nesting = 0;
/*  557 */           for (int i = 0; i < newWrappers.length; i++) {
/*  558 */             int slashCount = slashCount(newWrappers[i].name);
/*  559 */             if (slashCount > context.nesting) {
/*  560 */               context.nesting = slashCount;
/*      */             }
/*      */           }
/*  563 */           context.wildcardWrappers = newWrappers;
/*      */         }
/*  565 */       } else if (path.startsWith("*."))
/*      */       {
/*  567 */         String name = path.substring(2);
/*  568 */         MappedWrapper[] oldWrappers = context.extensionWrappers;
/*  569 */         if (oldWrappers.length == 0) {
/*  570 */           return;
/*      */         }
/*  572 */         MappedWrapper[] newWrappers = new MappedWrapper[oldWrappers.length - 1];
/*      */         
/*  574 */         if (removeMap(oldWrappers, newWrappers, name)) {
/*  575 */           context.extensionWrappers = newWrappers;
/*      */         }
/*  577 */       } else if (path.equals("/"))
/*      */       {
/*  579 */         context.defaultWrapper = null;
/*      */       } else {
/*      */         String name;
/*      */         String name;
/*  583 */         if (path.length() == 0)
/*      */         {
/*      */ 
/*  586 */           name = "/";
/*      */         } else {
/*  588 */           name = path;
/*      */         }
/*  590 */         MappedWrapper[] oldWrappers = context.exactWrappers;
/*  591 */         if (oldWrappers.length == 0) {
/*  592 */           return;
/*      */         }
/*  594 */         MappedWrapper[] newWrappers = new MappedWrapper[oldWrappers.length - 1];
/*      */         
/*  596 */         if (removeMap(oldWrappers, newWrappers, name)) {
/*  597 */           context.exactWrappers = newWrappers;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addWelcomeFile(String hostName, String contextPath, String version, String welcomeFile)
/*      */   {
/*  614 */     hostName = renameWildcardHost(hostName);
/*  615 */     ContextVersion contextVersion = findContextVersion(hostName, contextPath, version, false);
/*  616 */     if (contextVersion == null) {
/*  617 */       return;
/*      */     }
/*  619 */     int len = contextVersion.welcomeResources.length + 1;
/*  620 */     String[] newWelcomeResources = new String[len];
/*  621 */     System.arraycopy(contextVersion.welcomeResources, 0, newWelcomeResources, 0, len - 1);
/*  622 */     newWelcomeResources[(len - 1)] = welcomeFile;
/*  623 */     contextVersion.welcomeResources = newWelcomeResources;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeWelcomeFile(String hostName, String contextPath, String version, String welcomeFile)
/*      */   {
/*  637 */     hostName = renameWildcardHost(hostName);
/*  638 */     ContextVersion contextVersion = findContextVersion(hostName, contextPath, version, false);
/*  639 */     if ((contextVersion == null) || (contextVersion.isPaused())) {
/*  640 */       return;
/*      */     }
/*  642 */     int match = -1;
/*  643 */     for (int i = 0; i < contextVersion.welcomeResources.length; i++) {
/*  644 */       if (welcomeFile.equals(contextVersion.welcomeResources[i])) {
/*  645 */         match = i;
/*  646 */         break;
/*      */       }
/*      */     }
/*  649 */     if (match > -1) {
/*  650 */       int len = contextVersion.welcomeResources.length - 1;
/*  651 */       String[] newWelcomeResources = new String[len];
/*  652 */       System.arraycopy(contextVersion.welcomeResources, 0, newWelcomeResources, 0, match);
/*  653 */       if (match < len) {
/*  654 */         System.arraycopy(contextVersion.welcomeResources, match + 1, newWelcomeResources, match, len - match);
/*      */       }
/*      */       
/*  657 */       contextVersion.welcomeResources = newWelcomeResources;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearWelcomeFiles(String hostName, String contextPath, String version)
/*      */   {
/*  670 */     hostName = renameWildcardHost(hostName);
/*  671 */     ContextVersion contextVersion = findContextVersion(hostName, contextPath, version, false);
/*  672 */     if (contextVersion == null) {
/*  673 */       return;
/*      */     }
/*  675 */     contextVersion.welcomeResources = new String[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void map(MessageBytes host, MessageBytes uri, String version, MappingData mappingData)
/*      */     throws IOException
/*      */   {
/*  693 */     if (host.isNull()) {
/*  694 */       host.getCharChunk().append(this.defaultHostName);
/*      */     }
/*  696 */     host.toChars();
/*  697 */     uri.toChars();
/*  698 */     internalMap(host.getCharChunk(), uri.getCharChunk(), version, mappingData);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void map(Context context, MessageBytes uri, MappingData mappingData)
/*      */     throws IOException
/*      */   {
/*  718 */     ContextVersion contextVersion = (ContextVersion)this.contextObjectToContextVersionMap.get(context);
/*  719 */     uri.toChars();
/*  720 */     CharChunk uricc = uri.getCharChunk();
/*  721 */     uricc.setLimit(-1);
/*  722 */     internalMapWrapper(contextVersion, uricc, mappingData);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void internalMap(CharChunk host, CharChunk uri, String version, MappingData mappingData)
/*      */     throws IOException
/*      */   {
/*  735 */     if (mappingData.host != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  740 */       throw new AssertionError();
/*      */     }
/*      */     
/*  743 */     uri.setLimit(-1);
/*      */     
/*      */ 
/*  746 */     MappedHost[] hosts = this.hosts;
/*  747 */     MappedHost mappedHost = (MappedHost)exactFindIgnoreCase(hosts, host);
/*  748 */     if (mappedHost == null)
/*      */     {
/*      */ 
/*  751 */       int firstDot = host.indexOf('.');
/*  752 */       if (firstDot > -1) {
/*  753 */         int offset = host.getOffset();
/*      */         try {
/*  755 */           host.setOffset(firstDot + offset);
/*  756 */           mappedHost = (MappedHost)exactFindIgnoreCase(hosts, host);
/*      */         }
/*      */         finally {
/*  759 */           host.setOffset(offset);
/*      */         }
/*      */       }
/*  762 */       if (mappedHost == null) {
/*  763 */         mappedHost = this.defaultHost;
/*  764 */         if (mappedHost == null) {
/*  765 */           return;
/*      */         }
/*      */       }
/*      */     }
/*  769 */     mappingData.host = ((Host)mappedHost.object);
/*      */     
/*      */ 
/*  772 */     ContextList contextList = mappedHost.contextList;
/*  773 */     MappedContext[] contexts = contextList.contexts;
/*  774 */     int pos = find(contexts, uri);
/*  775 */     if (pos == -1) {
/*  776 */       return;
/*      */     }
/*      */     
/*  779 */     int lastSlash = -1;
/*  780 */     int uriEnd = uri.getEnd();
/*  781 */     int length = -1;
/*  782 */     boolean found = false;
/*  783 */     MappedContext context = null;
/*  784 */     while (pos >= 0) {
/*  785 */       context = contexts[pos];
/*  786 */       if (uri.startsWith(context.name)) {
/*  787 */         length = context.name.length();
/*  788 */         if (uri.getLength() == length) {
/*  789 */           found = true;
/*  790 */           break; }
/*  791 */         if (uri.startsWithIgnoreCase("/", length)) {
/*  792 */           found = true;
/*  793 */           break;
/*      */         }
/*      */       }
/*  796 */       if (lastSlash == -1) {
/*  797 */         lastSlash = nthSlash(uri, contextList.nesting + 1);
/*      */       } else {
/*  799 */         lastSlash = lastSlash(uri);
/*      */       }
/*  801 */       uri.setEnd(lastSlash);
/*  802 */       pos = find(contexts, uri);
/*      */     }
/*  804 */     uri.setEnd(uriEnd);
/*      */     
/*  806 */     if (!found) {
/*  807 */       if (contexts[0].name.equals("")) {
/*  808 */         context = contexts[0];
/*      */       } else {
/*  810 */         context = null;
/*      */       }
/*      */     }
/*  813 */     if (context == null) {
/*  814 */       return;
/*      */     }
/*      */     
/*  817 */     mappingData.contextPath.setString(context.name);
/*      */     
/*  819 */     ContextVersion contextVersion = null;
/*  820 */     ContextVersion[] contextVersions = context.versions;
/*  821 */     int versionCount = contextVersions.length;
/*  822 */     if (versionCount > 1) {
/*  823 */       Context[] contextObjects = new Context[contextVersions.length];
/*  824 */       for (int i = 0; i < contextObjects.length; i++) {
/*  825 */         contextObjects[i] = ((Context)contextVersions[i].object);
/*      */       }
/*  827 */       mappingData.contexts = contextObjects;
/*  828 */       if (version != null) {
/*  829 */         contextVersion = (ContextVersion)exactFind(contextVersions, version);
/*      */       }
/*      */     }
/*  832 */     if (contextVersion == null)
/*      */     {
/*      */ 
/*  835 */       contextVersion = contextVersions[(versionCount - 1)];
/*      */     }
/*  837 */     mappingData.context = ((Context)contextVersion.object);
/*  838 */     mappingData.contextSlashCount = contextVersion.slashCount;
/*      */     
/*      */ 
/*  841 */     if (!contextVersion.isPaused()) {
/*  842 */       internalMapWrapper(contextVersion, uri, mappingData);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void internalMapWrapper(ContextVersion contextVersion, CharChunk path, MappingData mappingData)
/*      */     throws IOException
/*      */   {
/*  857 */     int pathOffset = path.getOffset();
/*  858 */     int pathEnd = path.getEnd();
/*  859 */     boolean noServletPath = false;
/*      */     
/*  861 */     int length = contextVersion.path.length();
/*  862 */     if (length == pathEnd - pathOffset) {
/*  863 */       noServletPath = true;
/*      */     }
/*  865 */     int servletPath = pathOffset + length;
/*  866 */     path.setOffset(servletPath);
/*      */     
/*      */ 
/*  869 */     MappedWrapper[] exactWrappers = contextVersion.exactWrappers;
/*  870 */     internalMapExactWrapper(exactWrappers, path, mappingData);
/*      */     
/*      */ 
/*  873 */     boolean checkJspWelcomeFiles = false;
/*  874 */     MappedWrapper[] wildcardWrappers = contextVersion.wildcardWrappers;
/*  875 */     if (mappingData.wrapper == null) {
/*  876 */       internalMapWildcardWrapper(wildcardWrappers, contextVersion.nesting, path, mappingData);
/*      */       
/*  878 */       if ((mappingData.wrapper != null) && (mappingData.jspWildCard)) {
/*  879 */         char[] buf = path.getBuffer();
/*  880 */         if (buf[(pathEnd - 1)] == '/')
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  889 */           mappingData.wrapper = null;
/*  890 */           checkJspWelcomeFiles = true;
/*      */         }
/*      */         else {
/*  893 */           mappingData.wrapperPath.setChars(buf, path.getStart(), path
/*  894 */             .getLength());
/*  895 */           mappingData.pathInfo.recycle();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  900 */     if ((mappingData.wrapper == null) && (noServletPath) && 
/*  901 */       (((Context)contextVersion.object).getMapperContextRootRedirectEnabled()))
/*      */     {
/*  903 */       path.append('/');
/*  904 */       pathEnd = path.getEnd();
/*  905 */       mappingData.redirectPath
/*  906 */         .setChars(path.getBuffer(), pathOffset, pathEnd - pathOffset);
/*  907 */       path.setEnd(pathEnd - 1);
/*  908 */       return;
/*      */     }
/*      */     
/*      */ 
/*  912 */     MappedWrapper[] extensionWrappers = contextVersion.extensionWrappers;
/*  913 */     if ((mappingData.wrapper == null) && (!checkJspWelcomeFiles)) {
/*  914 */       internalMapExtensionWrapper(extensionWrappers, path, mappingData, true);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  919 */     if (mappingData.wrapper == null) {
/*  920 */       boolean checkWelcomeFiles = checkJspWelcomeFiles;
/*  921 */       if (!checkWelcomeFiles) {
/*  922 */         char[] buf = path.getBuffer();
/*  923 */         checkWelcomeFiles = buf[(pathEnd - 1)] == '/';
/*      */       }
/*  925 */       if (checkWelcomeFiles) {
/*  926 */         for (int i = 0; 
/*  927 */             (i < contextVersion.welcomeResources.length) && (mappingData.wrapper == null); i++) {
/*  928 */           path.setOffset(pathOffset);
/*  929 */           path.setEnd(pathEnd);
/*  930 */           path.append(contextVersion.welcomeResources[i], 0, contextVersion.welcomeResources[i]
/*  931 */             .length());
/*  932 */           path.setOffset(servletPath);
/*      */           
/*      */ 
/*  935 */           internalMapExactWrapper(exactWrappers, path, mappingData);
/*      */           
/*      */ 
/*  938 */           if (mappingData.wrapper == null)
/*      */           {
/*  940 */             internalMapWildcardWrapper(wildcardWrappers, contextVersion.nesting, path, mappingData);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  946 */           if ((mappingData.wrapper == null) && (contextVersion.resources != null))
/*      */           {
/*  948 */             String pathStr = path.toString();
/*      */             
/*  950 */             WebResource file = contextVersion.resources.getResource(pathStr);
/*  951 */             if ((file != null) && (file.isFile())) {
/*  952 */               internalMapExtensionWrapper(extensionWrappers, path, mappingData, true);
/*      */               
/*  954 */               if ((mappingData.wrapper == null) && (contextVersion.defaultWrapper != null))
/*      */               {
/*  956 */                 mappingData.wrapper = ((Wrapper)contextVersion.defaultWrapper.object);
/*      */                 
/*  958 */                 mappingData.requestPath
/*  959 */                   .setChars(path.getBuffer(), path.getStart(), path
/*  960 */                   .getLength());
/*  961 */                 mappingData.wrapperPath
/*  962 */                   .setChars(path.getBuffer(), path.getStart(), path
/*  963 */                   .getLength());
/*  964 */                 mappingData.requestPath.setString(pathStr);
/*  965 */                 mappingData.wrapperPath.setString(pathStr);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  971 */         path.setOffset(servletPath);
/*  972 */         path.setEnd(pathEnd);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  984 */     if (mappingData.wrapper == null) {
/*  985 */       boolean checkWelcomeFiles = checkJspWelcomeFiles;
/*  986 */       if (!checkWelcomeFiles) {
/*  987 */         char[] buf = path.getBuffer();
/*  988 */         checkWelcomeFiles = buf[(pathEnd - 1)] == '/';
/*      */       }
/*  990 */       if (checkWelcomeFiles) {
/*  991 */         for (int i = 0; 
/*  992 */             (i < contextVersion.welcomeResources.length) && (mappingData.wrapper == null); i++) {
/*  993 */           path.setOffset(pathOffset);
/*  994 */           path.setEnd(pathEnd);
/*  995 */           path.append(contextVersion.welcomeResources[i], 0, contextVersion.welcomeResources[i]
/*  996 */             .length());
/*  997 */           path.setOffset(servletPath);
/*  998 */           internalMapExtensionWrapper(extensionWrappers, path, mappingData, false);
/*      */         }
/*      */         
/*      */ 
/* 1002 */         path.setOffset(servletPath);
/* 1003 */         path.setEnd(pathEnd);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1009 */     if ((mappingData.wrapper == null) && (!checkJspWelcomeFiles)) {
/* 1010 */       if (contextVersion.defaultWrapper != null) {
/* 1011 */         mappingData.wrapper = ((Wrapper)contextVersion.defaultWrapper.object);
/* 1012 */         mappingData.requestPath
/* 1013 */           .setChars(path.getBuffer(), path.getStart(), path.getLength());
/* 1014 */         mappingData.wrapperPath
/* 1015 */           .setChars(path.getBuffer(), path.getStart(), path.getLength());
/* 1016 */         mappingData.matchType = MappingMatch.DEFAULT;
/*      */       }
/*      */       
/* 1019 */       char[] buf = path.getBuffer();
/* 1020 */       if ((contextVersion.resources != null) && (buf[(pathEnd - 1)] != '/')) {
/* 1021 */         String pathStr = path.toString();
/*      */         WebResource file;
/*      */         WebResource file;
/* 1024 */         if (pathStr.length() == 0) {
/* 1025 */           file = contextVersion.resources.getResource("/");
/*      */         } else {
/* 1027 */           file = contextVersion.resources.getResource(pathStr);
/*      */         }
/* 1029 */         if ((file != null) && (file.isDirectory()) && 
/* 1030 */           (((Context)contextVersion.object).getMapperDirectoryRedirectEnabled()))
/*      */         {
/*      */ 
/*      */ 
/* 1034 */           path.setOffset(pathOffset);
/* 1035 */           path.append('/');
/* 1036 */           mappingData.redirectPath
/* 1037 */             .setChars(path.getBuffer(), path.getStart(), path.getLength());
/*      */         } else {
/* 1039 */           mappingData.requestPath.setString(pathStr);
/* 1040 */           mappingData.wrapperPath.setString(pathStr);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1045 */     path.setOffset(pathOffset);
/* 1046 */     path.setEnd(pathEnd);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void internalMapExactWrapper(MappedWrapper[] wrappers, CharChunk path, MappingData mappingData)
/*      */   {
/* 1055 */     MappedWrapper wrapper = (MappedWrapper)exactFind(wrappers, path);
/* 1056 */     if (wrapper != null) {
/* 1057 */       mappingData.requestPath.setString(wrapper.name);
/* 1058 */       mappingData.wrapper = ((Wrapper)wrapper.object);
/* 1059 */       if (path.equals("/"))
/*      */       {
/* 1061 */         mappingData.pathInfo.setString("/");
/* 1062 */         mappingData.wrapperPath.setString("");
/*      */         
/* 1064 */         mappingData.contextPath.setString("");
/* 1065 */         mappingData.matchType = MappingMatch.CONTEXT_ROOT;
/*      */       } else {
/* 1067 */         mappingData.wrapperPath.setString(wrapper.name);
/* 1068 */         mappingData.matchType = MappingMatch.EXACT;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void internalMapWildcardWrapper(MappedWrapper[] wrappers, int nesting, CharChunk path, MappingData mappingData)
/*      */   {
/* 1081 */     int pathEnd = path.getEnd();
/*      */     
/* 1083 */     int lastSlash = -1;
/* 1084 */     int length = -1;
/* 1085 */     int pos = find(wrappers, path);
/* 1086 */     if (pos != -1) {
/* 1087 */       boolean found = false;
/* 1088 */       while (pos >= 0) {
/* 1089 */         if (path.startsWith(wrappers[pos].name)) {
/* 1090 */           length = wrappers[pos].name.length();
/* 1091 */           if (path.getLength() == length) {
/* 1092 */             found = true;
/* 1093 */             break; }
/* 1094 */           if (path.startsWithIgnoreCase("/", length)) {
/* 1095 */             found = true;
/* 1096 */             break;
/*      */           }
/*      */         }
/* 1099 */         if (lastSlash == -1) {
/* 1100 */           lastSlash = nthSlash(path, nesting + 1);
/*      */         } else {
/* 1102 */           lastSlash = lastSlash(path);
/*      */         }
/* 1104 */         path.setEnd(lastSlash);
/* 1105 */         pos = find(wrappers, path);
/*      */       }
/* 1107 */       path.setEnd(pathEnd);
/* 1108 */       if (found) {
/* 1109 */         mappingData.wrapperPath.setString(wrappers[pos].name);
/* 1110 */         if (path.getLength() > length)
/*      */         {
/* 1112 */           mappingData.pathInfo.setChars(path.getBuffer(), path
/* 1113 */             .getOffset() + length, path
/* 1114 */             .getLength() - length);
/*      */         }
/*      */         
/* 1117 */         mappingData.requestPath.setChars(path.getBuffer(), path.getOffset(), path.getLength());
/* 1118 */         mappingData.wrapper = ((Wrapper)wrappers[pos].object);
/* 1119 */         mappingData.jspWildCard = wrappers[pos].jspWildCard;
/* 1120 */         mappingData.matchType = MappingMatch.PATH;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void internalMapExtensionWrapper(MappedWrapper[] wrappers, CharChunk path, MappingData mappingData, boolean resourceExpected)
/*      */   {
/* 1136 */     char[] buf = path.getBuffer();
/* 1137 */     int pathEnd = path.getEnd();
/* 1138 */     int servletPath = path.getOffset();
/* 1139 */     int slash = -1;
/* 1140 */     for (int i = pathEnd - 1; i >= servletPath; i--) {
/* 1141 */       if (buf[i] == '/') {
/* 1142 */         slash = i;
/* 1143 */         break;
/*      */       }
/*      */     }
/* 1146 */     if (slash >= 0) {
/* 1147 */       int period = -1;
/* 1148 */       for (int i = pathEnd - 1; i > slash; i--) {
/* 1149 */         if (buf[i] == '.') {
/* 1150 */           period = i;
/* 1151 */           break;
/*      */         }
/*      */       }
/* 1154 */       if (period >= 0) {
/* 1155 */         path.setOffset(period + 1);
/* 1156 */         path.setEnd(pathEnd);
/* 1157 */         MappedWrapper wrapper = (MappedWrapper)exactFind(wrappers, path);
/* 1158 */         if ((wrapper != null) && ((resourceExpected) || (!wrapper.resourceOnly)))
/*      */         {
/* 1160 */           mappingData.wrapperPath.setChars(buf, servletPath, pathEnd - servletPath);
/*      */           
/* 1162 */           mappingData.requestPath.setChars(buf, servletPath, pathEnd - servletPath);
/*      */           
/* 1164 */           mappingData.wrapper = ((Wrapper)wrapper.object);
/* 1165 */           mappingData.matchType = MappingMatch.EXTENSION;
/*      */         }
/* 1167 */         path.setOffset(servletPath);
/* 1168 */         path.setEnd(pathEnd);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T> int find(MapElement<T>[] map, CharChunk name)
/*      */   {
/* 1180 */     return find(map, name, name.getStart(), name.getEnd());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T> int find(MapElement<T>[] map, CharChunk name, int start, int end)
/*      */   {
/* 1192 */     int a = 0;
/* 1193 */     int b = map.length - 1;
/*      */     
/*      */ 
/* 1196 */     if (b == -1) {
/* 1197 */       return -1;
/*      */     }
/*      */     
/* 1200 */     if (compare(name, start, end, map[0].name) < 0) {
/* 1201 */       return -1;
/*      */     }
/* 1203 */     if (b == 0) {
/* 1204 */       return 0;
/*      */     }
/*      */     
/* 1207 */     int i = 0;
/*      */     for (;;) {
/* 1209 */       i = (b + a) / 2;
/* 1210 */       int result = compare(name, start, end, map[i].name);
/* 1211 */       if (result == 1) {
/* 1212 */         a = i;
/* 1213 */       } else { if (result == 0) {
/* 1214 */           return i;
/*      */         }
/* 1216 */         b = i;
/*      */       }
/* 1218 */       if (b - a == 1) {
/* 1219 */         int result2 = compare(name, start, end, map[b].name);
/* 1220 */         if (result2 < 0) {
/* 1221 */           return a;
/*      */         }
/* 1223 */         return b;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T> int findIgnoreCase(MapElement<T>[] map, CharChunk name)
/*      */   {
/* 1236 */     return findIgnoreCase(map, name, name.getStart(), name.getEnd());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T> int findIgnoreCase(MapElement<T>[] map, CharChunk name, int start, int end)
/*      */   {
/* 1248 */     int a = 0;
/* 1249 */     int b = map.length - 1;
/*      */     
/*      */ 
/* 1252 */     if (b == -1) {
/* 1253 */       return -1;
/*      */     }
/* 1255 */     if (compareIgnoreCase(name, start, end, map[0].name) < 0) {
/* 1256 */       return -1;
/*      */     }
/* 1258 */     if (b == 0) {
/* 1259 */       return 0;
/*      */     }
/*      */     
/* 1262 */     int i = 0;
/*      */     for (;;) {
/* 1264 */       i = (b + a) / 2;
/* 1265 */       int result = compareIgnoreCase(name, start, end, map[i].name);
/* 1266 */       if (result == 1) {
/* 1267 */         a = i;
/* 1268 */       } else { if (result == 0) {
/* 1269 */           return i;
/*      */         }
/* 1271 */         b = i;
/*      */       }
/* 1273 */       if (b - a == 1) {
/* 1274 */         int result2 = compareIgnoreCase(name, start, end, map[b].name);
/* 1275 */         if (result2 < 0) {
/* 1276 */           return a;
/*      */         }
/* 1278 */         return b;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T> int find(MapElement<T>[] map, String name)
/*      */   {
/* 1294 */     int a = 0;
/* 1295 */     int b = map.length - 1;
/*      */     
/*      */ 
/* 1298 */     if (b == -1) {
/* 1299 */       return -1;
/*      */     }
/*      */     
/* 1302 */     if (name.compareTo(map[0].name) < 0) {
/* 1303 */       return -1;
/*      */     }
/* 1305 */     if (b == 0) {
/* 1306 */       return 0;
/*      */     }
/*      */     
/* 1309 */     int i = 0;
/*      */     for (;;) {
/* 1311 */       i = (b + a) / 2;
/* 1312 */       int result = name.compareTo(map[i].name);
/* 1313 */       if (result > 0) {
/* 1314 */         a = i;
/* 1315 */       } else { if (result == 0) {
/* 1316 */           return i;
/*      */         }
/* 1318 */         b = i;
/*      */       }
/* 1320 */       if (b - a == 1) {
/* 1321 */         int result2 = name.compareTo(map[b].name);
/* 1322 */         if (result2 < 0) {
/* 1323 */           return a;
/*      */         }
/* 1325 */         return b;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T, E extends MapElement<T>> E exactFind(E[] map, String name)
/*      */   {
/* 1341 */     int pos = find(map, name);
/* 1342 */     if (pos >= 0) {
/* 1343 */       E result = map[pos];
/* 1344 */       if (name.equals(result.name)) {
/* 1345 */         return result;
/*      */       }
/*      */     }
/* 1348 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T, E extends MapElement<T>> E exactFind(E[] map, CharChunk name)
/*      */   {
/* 1358 */     int pos = find(map, name);
/* 1359 */     if (pos >= 0) {
/* 1360 */       E result = map[pos];
/* 1361 */       if (name.equals(result.name)) {
/* 1362 */         return result;
/*      */       }
/*      */     }
/* 1365 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T, E extends MapElement<T>> E exactFindIgnoreCase(E[] map, CharChunk name)
/*      */   {
/* 1376 */     int pos = findIgnoreCase(map, name);
/* 1377 */     if (pos >= 0) {
/* 1378 */       E result = map[pos];
/* 1379 */       if (name.equalsIgnoreCase(result.name)) {
/* 1380 */         return result;
/*      */       }
/*      */     }
/* 1383 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int compare(CharChunk name, int start, int end, String compareTo)
/*      */   {
/* 1393 */     int result = 0;
/* 1394 */     char[] c = name.getBuffer();
/* 1395 */     int len = compareTo.length();
/* 1396 */     if (end - start < len) {
/* 1397 */       len = end - start;
/*      */     }
/* 1399 */     for (int i = 0; (i < len) && (result == 0); i++) {
/* 1400 */       if (c[(i + start)] > compareTo.charAt(i)) {
/* 1401 */         result = 1;
/* 1402 */       } else if (c[(i + start)] < compareTo.charAt(i)) {
/* 1403 */         result = -1;
/*      */       }
/*      */     }
/* 1406 */     if (result == 0) {
/* 1407 */       if (compareTo.length() > end - start) {
/* 1408 */         result = -1;
/* 1409 */       } else if (compareTo.length() < end - start) {
/* 1410 */         result = 1;
/*      */       }
/*      */     }
/* 1413 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int compareIgnoreCase(CharChunk name, int start, int end, String compareTo)
/*      */   {
/* 1423 */     int result = 0;
/* 1424 */     char[] c = name.getBuffer();
/* 1425 */     int len = compareTo.length();
/* 1426 */     if (end - start < len) {
/* 1427 */       len = end - start;
/*      */     }
/* 1429 */     for (int i = 0; (i < len) && (result == 0); i++) {
/* 1430 */       if (Ascii.toLower(c[(i + start)]) > Ascii.toLower(compareTo.charAt(i))) {
/* 1431 */         result = 1;
/* 1432 */       } else if (Ascii.toLower(c[(i + start)]) < Ascii.toLower(compareTo.charAt(i))) {
/* 1433 */         result = -1;
/*      */       }
/*      */     }
/* 1436 */     if (result == 0) {
/* 1437 */       if (compareTo.length() > end - start) {
/* 1438 */         result = -1;
/* 1439 */       } else if (compareTo.length() < end - start) {
/* 1440 */         result = 1;
/*      */       }
/*      */     }
/* 1443 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int lastSlash(CharChunk name)
/*      */   {
/* 1452 */     char[] c = name.getBuffer();
/* 1453 */     int end = name.getEnd();
/* 1454 */     int start = name.getStart();
/* 1455 */     int pos = end;
/*      */     
/* 1457 */     while (pos > start) {
/* 1458 */       if (c[(--pos)] == '/') {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 1463 */     return pos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int nthSlash(CharChunk name, int n)
/*      */   {
/* 1473 */     char[] c = name.getBuffer();
/* 1474 */     int end = name.getEnd();
/* 1475 */     int start = name.getStart();
/* 1476 */     int pos = start;
/* 1477 */     int count = 0;
/*      */     
/* 1479 */     while (pos < end) {
/* 1480 */       if (c[(pos++)] == '/') { count++; if (count == n) {
/* 1481 */           pos--;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1486 */     return pos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int slashCount(String name)
/*      */   {
/* 1495 */     int pos = -1;
/* 1496 */     int count = 0;
/* 1497 */     while ((pos = name.indexOf('/', pos + 1)) != -1) {
/* 1498 */       count++;
/*      */     }
/* 1500 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T> boolean insertMap(MapElement<T>[] oldMap, MapElement<T>[] newMap, MapElement<T> newElement)
/*      */   {
/* 1510 */     int pos = find(oldMap, newElement.name);
/* 1511 */     if ((pos != -1) && (newElement.name.equals(oldMap[pos].name))) {
/* 1512 */       return false;
/*      */     }
/* 1514 */     System.arraycopy(oldMap, 0, newMap, 0, pos + 1);
/* 1515 */     newMap[(pos + 1)] = newElement;
/*      */     
/* 1517 */     System.arraycopy(oldMap, pos + 1, newMap, pos + 2, oldMap.length - pos - 1);
/* 1518 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T> boolean removeMap(MapElement<T>[] oldMap, MapElement<T>[] newMap, String name)
/*      */   {
/* 1527 */     int pos = find(oldMap, name);
/* 1528 */     if ((pos != -1) && (name.equals(oldMap[pos].name))) {
/* 1529 */       System.arraycopy(oldMap, 0, newMap, 0, pos);
/* 1530 */       System.arraycopy(oldMap, pos + 1, newMap, pos, oldMap.length - pos - 1);
/*      */       
/* 1532 */       return true;
/*      */     }
/* 1534 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String renameWildcardHost(String hostName)
/*      */   {
/* 1546 */     if (hostName.startsWith("*.")) {
/* 1547 */       return hostName.substring(1);
/*      */     }
/* 1549 */     return hostName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static abstract class MapElement<T>
/*      */   {
/*      */     public final String name;
/*      */     
/*      */     public final T object;
/*      */     
/*      */ 
/*      */     public MapElement(String name, T object)
/*      */     {
/* 1563 */       this.name = name;
/* 1564 */       this.object = object;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final class MappedHost
/*      */     extends Mapper.MapElement<Host>
/*      */   {
/*      */     public volatile Mapper.ContextList contextList;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final MappedHost realHost;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private final List<MappedHost> aliases;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public MappedHost(String name, Host host)
/*      */     {
/* 1595 */       super(host);
/* 1596 */       this.realHost = this;
/* 1597 */       this.contextList = new Mapper.ContextList();
/* 1598 */       this.aliases = new CopyOnWriteArrayList();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public MappedHost(String alias, MappedHost realHost)
/*      */     {
/* 1608 */       super(realHost.object);
/* 1609 */       this.realHost = realHost;
/* 1610 */       this.contextList = realHost.contextList;
/* 1611 */       this.aliases = null;
/*      */     }
/*      */     
/*      */     public boolean isAlias() {
/* 1615 */       return this.realHost != this;
/*      */     }
/*      */     
/*      */     public MappedHost getRealHost() {
/* 1619 */       return this.realHost;
/*      */     }
/*      */     
/*      */     public String getRealHostName() {
/* 1623 */       return this.realHost.name;
/*      */     }
/*      */     
/*      */     public Collection<MappedHost> getAliases() {
/* 1627 */       return this.aliases;
/*      */     }
/*      */     
/*      */     public void addAlias(MappedHost alias) {
/* 1631 */       this.aliases.add(alias);
/*      */     }
/*      */     
/*      */     public void addAliases(Collection<? extends MappedHost> c) {
/* 1635 */       this.aliases.addAll(c);
/*      */     }
/*      */     
/*      */     public void removeAlias(MappedHost alias) {
/* 1639 */       this.aliases.remove(alias);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static final class ContextList
/*      */   {
/*      */     public final Mapper.MappedContext[] contexts;
/*      */     
/*      */     public final int nesting;
/*      */     
/*      */ 
/*      */     public ContextList()
/*      */     {
/* 1653 */       this(new Mapper.MappedContext[0], 0);
/*      */     }
/*      */     
/*      */     private ContextList(Mapper.MappedContext[] contexts, int nesting) {
/* 1657 */       this.contexts = contexts;
/* 1658 */       this.nesting = nesting;
/*      */     }
/*      */     
/*      */     public ContextList addContext(Mapper.MappedContext mappedContext, int slashCount)
/*      */     {
/* 1663 */       Mapper.MappedContext[] newContexts = new Mapper.MappedContext[this.contexts.length + 1];
/* 1664 */       if (Mapper.insertMap(this.contexts, newContexts, mappedContext)) {
/* 1665 */         return new ContextList(newContexts, Math.max(this.nesting, slashCount));
/*      */       }
/*      */       
/* 1668 */       return null;
/*      */     }
/*      */     
/*      */     public ContextList removeContext(String path) {
/* 1672 */       Mapper.MappedContext[] newContexts = new Mapper.MappedContext[this.contexts.length - 1];
/* 1673 */       if (Mapper.removeMap(this.contexts, newContexts, path)) {
/* 1674 */         int newNesting = 0;
/* 1675 */         for (Mapper.MappedContext context : newContexts) {
/* 1676 */           newNesting = Math.max(newNesting, Mapper.slashCount(context.name));
/*      */         }
/* 1678 */         return new ContextList(newContexts, newNesting);
/*      */       }
/* 1680 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static final class MappedContext
/*      */     extends Mapper.MapElement<Void>
/*      */   {
/*      */     public volatile Mapper.ContextVersion[] versions;
/*      */     
/*      */     public MappedContext(String name, Mapper.ContextVersion firstVersion)
/*      */     {
/* 1692 */       super(null);
/* 1693 */       this.versions = new Mapper.ContextVersion[] { firstVersion };
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class ContextVersion extends Mapper.MapElement<Context> {
/*      */     public final String path;
/*      */     public final int slashCount;
/*      */     public final WebResourceRoot resources;
/*      */     public String[] welcomeResources;
/* 1702 */     public Mapper.MappedWrapper defaultWrapper = null;
/* 1703 */     public Mapper.MappedWrapper[] exactWrappers = new Mapper.MappedWrapper[0];
/* 1704 */     public Mapper.MappedWrapper[] wildcardWrappers = new Mapper.MappedWrapper[0];
/* 1705 */     public Mapper.MappedWrapper[] extensionWrappers = new Mapper.MappedWrapper[0];
/* 1706 */     public int nesting = 0;
/*      */     
/*      */     private volatile boolean paused;
/*      */     
/*      */     public ContextVersion(String version, String path, int slashCount, Context context, WebResourceRoot resources, String[] welcomeResources)
/*      */     {
/* 1712 */       super(context);
/* 1713 */       this.path = path;
/* 1714 */       this.slashCount = slashCount;
/* 1715 */       this.resources = resources;
/* 1716 */       this.welcomeResources = welcomeResources;
/*      */     }
/*      */     
/*      */     public boolean isPaused() {
/* 1720 */       return this.paused;
/*      */     }
/*      */     
/*      */     public void markPaused() {
/* 1724 */       this.paused = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static class MappedWrapper
/*      */     extends Mapper.MapElement<Wrapper>
/*      */   {
/*      */     public final boolean jspWildCard;
/*      */     
/*      */     public final boolean resourceOnly;
/*      */     
/*      */     public MappedWrapper(String name, Wrapper wrapper, boolean jspWildCard, boolean resourceOnly)
/*      */     {
/* 1738 */       super(wrapper);
/* 1739 */       this.jspWildCard = jspWildCard;
/* 1740 */       this.resourceOnly = resourceOnly;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mapper\Mapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */