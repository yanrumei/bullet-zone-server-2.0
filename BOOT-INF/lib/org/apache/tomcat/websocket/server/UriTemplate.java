/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.websocket.DeploymentException;
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
/*     */ public class UriTemplate
/*     */ {
/*  37 */   private static final StringManager sm = StringManager.getManager(UriTemplate.class);
/*     */   
/*     */   private final String normalized;
/*  40 */   private final List<Segment> segments = new ArrayList();
/*     */   private final boolean hasParameters;
/*     */   
/*     */   public UriTemplate(String path)
/*     */     throws DeploymentException
/*     */   {
/*  46 */     if ((path == null) || (path.length() == 0) || (!path.startsWith("/")))
/*     */     {
/*  48 */       throw new DeploymentException(sm.getString("uriTemplate.invalidPath", new Object[] { path }));
/*     */     }
/*     */     
/*  51 */     StringBuilder normalized = new StringBuilder(path.length());
/*  52 */     Set<String> paramNames = new HashSet();
/*     */     
/*     */ 
/*  55 */     String[] segments = path.split("/", -1);
/*  56 */     int paramCount = 0;
/*  57 */     int segmentCount = 0;
/*     */     
/*  59 */     for (int i = 0; i < segments.length; i++) {
/*  60 */       String segment = segments[i];
/*  61 */       if (segment.length() == 0) {
/*  62 */         if ((i != 0) && ((i != segments.length - 1) || (paramCount != 0)))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */           throw new IllegalArgumentException(sm.getString("uriTemplate.emptySegment", new Object[] { path }));
/*     */         }
/*     */       }
/*     */       else {
/*  75 */         normalized.append('/');
/*  76 */         int index = -1;
/*  77 */         if ((segment.startsWith("{")) && (segment.endsWith("}"))) {
/*  78 */           index = segmentCount;
/*  79 */           segment = segment.substring(1, segment.length() - 1);
/*  80 */           normalized.append('{');
/*  81 */           normalized.append(paramCount++);
/*  82 */           normalized.append('}');
/*  83 */           if (!paramNames.add(segment)) {
/*  84 */             throw new IllegalArgumentException(sm.getString("uriTemplate.duplicateParameter", new Object[] { segment }));
/*     */           }
/*     */         }
/*     */         else {
/*  88 */           if ((segment.contains("{")) || (segment.contains("}"))) {
/*  89 */             throw new IllegalArgumentException(sm.getString("uriTemplate.invalidSegment", new Object[] { segment, path }));
/*     */           }
/*     */           
/*  92 */           normalized.append(segment);
/*     */         }
/*  94 */         this.segments.add(new Segment(index, segment));
/*  95 */         segmentCount++;
/*     */       }
/*     */     }
/*  98 */     this.normalized = normalized.toString();
/*  99 */     this.hasParameters = (paramCount > 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, String> match(UriTemplate candidate)
/*     */   {
/* 105 */     Map<String, String> result = new HashMap();
/*     */     
/*     */ 
/* 108 */     if (candidate.getSegmentCount() != getSegmentCount()) {
/* 109 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 113 */     Iterator<Segment> candidateSegments = candidate.getSegments().iterator();
/* 114 */     Iterator<Segment> targetSegments = this.segments.iterator();
/*     */     
/* 116 */     while (candidateSegments.hasNext()) {
/* 117 */       Segment candidateSegment = (Segment)candidateSegments.next();
/* 118 */       Segment targetSegment = (Segment)targetSegments.next();
/*     */       
/* 120 */       if (targetSegment.getParameterIndex() == -1)
/*     */       {
/* 122 */         if (!targetSegment.getValue().equals(candidateSegment
/* 123 */           .getValue()))
/*     */         {
/* 125 */           return null;
/*     */         }
/*     */       }
/*     */       else {
/* 129 */         result.put(targetSegment.getValue(), candidateSegment
/* 130 */           .getValue());
/*     */       }
/*     */     }
/*     */     
/* 134 */     return result;
/*     */   }
/*     */   
/*     */   public boolean hasParameters()
/*     */   {
/* 139 */     return this.hasParameters;
/*     */   }
/*     */   
/*     */   public int getSegmentCount()
/*     */   {
/* 144 */     return this.segments.size();
/*     */   }
/*     */   
/*     */   public String getNormalizedPath()
/*     */   {
/* 149 */     return this.normalized;
/*     */   }
/*     */   
/*     */   private List<Segment> getSegments()
/*     */   {
/* 154 */     return this.segments;
/*     */   }
/*     */   
/*     */   private static class Segment
/*     */   {
/*     */     private final int parameterIndex;
/*     */     private final String value;
/*     */     
/*     */     public Segment(int parameterIndex, String value) {
/* 163 */       this.parameterIndex = parameterIndex;
/* 164 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int getParameterIndex()
/*     */     {
/* 169 */       return this.parameterIndex;
/*     */     }
/*     */     
/*     */     public String getValue()
/*     */     {
/* 174 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\UriTemplate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */