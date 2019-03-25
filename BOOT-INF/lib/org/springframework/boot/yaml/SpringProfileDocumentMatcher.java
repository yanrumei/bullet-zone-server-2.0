/*     */ package org.springframework.boot.yaml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.config.YamlProcessor.DocumentMatcher;
/*     */ import org.springframework.beans.factory.config.YamlProcessor.MatchStatus;
/*     */ import org.springframework.boot.bind.PropertySourcesPropertyValues;
/*     */ import org.springframework.boot.bind.RelaxedDataBinder;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringProfileDocumentMatcher
/*     */   implements YamlProcessor.DocumentMatcher
/*     */ {
/*  53 */   private String[] activeProfiles = new String[0];
/*     */   
/*     */   public SpringProfileDocumentMatcher() {}
/*     */   
/*     */   public SpringProfileDocumentMatcher(String... profiles)
/*     */   {
/*  59 */     addActiveProfiles(profiles);
/*     */   }
/*     */   
/*     */   public void addActiveProfiles(String... profiles)
/*     */   {
/*  64 */     LinkedHashSet<String> set = new LinkedHashSet(Arrays.asList(this.activeProfiles));
/*  65 */     Collections.addAll(set, profiles);
/*  66 */     this.activeProfiles = ((String[])set.toArray(new String[set.size()]));
/*     */   }
/*     */   
/*     */   public YamlProcessor.MatchStatus matches(Properties properties)
/*     */   {
/*  71 */     List<String> profiles = extractSpringProfiles(properties);
/*  72 */     ProfilesMatcher profilesMatcher = getProfilesMatcher();
/*  73 */     Set<String> negative = extractProfiles(profiles, ProfileType.NEGATIVE);
/*  74 */     Set<String> positive = extractProfiles(profiles, ProfileType.POSITIVE);
/*  75 */     if (!CollectionUtils.isEmpty(negative)) {
/*  76 */       if (profilesMatcher.matches(negative) == YamlProcessor.MatchStatus.FOUND) {
/*  77 */         return YamlProcessor.MatchStatus.NOT_FOUND;
/*     */       }
/*  79 */       if (CollectionUtils.isEmpty(positive)) {
/*  80 */         return YamlProcessor.MatchStatus.FOUND;
/*     */       }
/*     */     }
/*  83 */     return profilesMatcher.matches(positive);
/*     */   }
/*     */   
/*     */   private List<String> extractSpringProfiles(Properties properties) {
/*  87 */     SpringProperties springProperties = new SpringProperties();
/*  88 */     MutablePropertySources propertySources = new MutablePropertySources();
/*  89 */     propertySources.addFirst(new PropertiesPropertySource("profiles", properties));
/*  90 */     PropertyValues propertyValues = new PropertySourcesPropertyValues(propertySources);
/*     */     
/*  92 */     new RelaxedDataBinder(springProperties, "spring").bind(propertyValues);
/*  93 */     List<String> profiles = springProperties.getProfiles();
/*  94 */     return profiles;
/*     */   }
/*     */   
/*     */   private ProfilesMatcher getProfilesMatcher() {
/*  98 */     return this.activeProfiles.length == 0 ? new EmptyProfilesMatcher(null) : new ActiveProfilesMatcher(new HashSet(
/*     */     
/* 100 */       Arrays.asList(this.activeProfiles)));
/*     */   }
/*     */   
/*     */   private Set<String> extractProfiles(List<String> profiles, ProfileType type) {
/* 104 */     if (CollectionUtils.isEmpty(profiles)) {
/* 105 */       return null;
/*     */     }
/* 107 */     Set<String> extractedProfiles = new HashSet();
/* 108 */     for (String candidate : profiles) {
/* 109 */       ProfileType candidateType = ProfileType.POSITIVE;
/* 110 */       if (candidate.startsWith("!")) {
/* 111 */         candidateType = ProfileType.NEGATIVE;
/*     */       }
/* 113 */       if (candidateType == type) {
/* 114 */         extractedProfiles.add(type == ProfileType.POSITIVE ? candidate : candidate
/* 115 */           .substring(1));
/*     */       }
/*     */     }
/* 118 */     return extractedProfiles;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static enum ProfileType
/*     */   {
/* 126 */     POSITIVE,  NEGATIVE;
/*     */     
/*     */ 
/*     */     private ProfileType() {}
/*     */   }
/*     */   
/*     */   private static abstract class ProfilesMatcher
/*     */   {
/*     */     public final YamlProcessor.MatchStatus matches(Set<String> profiles)
/*     */     {
/* 136 */       if (CollectionUtils.isEmpty(profiles)) {
/* 137 */         return YamlProcessor.MatchStatus.ABSTAIN;
/*     */       }
/* 139 */       return doMatches(profiles);
/*     */     }
/*     */     
/*     */ 
/*     */     protected abstract YamlProcessor.MatchStatus doMatches(Set<String> paramSet);
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ActiveProfilesMatcher
/*     */     extends SpringProfileDocumentMatcher.ProfilesMatcher
/*     */   {
/*     */     private final Set<String> activeProfiles;
/*     */     
/*     */     ActiveProfilesMatcher(Set<String> activeProfiles)
/*     */     {
/* 154 */       super();
/* 155 */       this.activeProfiles = activeProfiles;
/*     */     }
/*     */     
/*     */     protected YamlProcessor.MatchStatus doMatches(Set<String> profiles)
/*     */     {
/* 160 */       if (profiles.isEmpty()) {
/* 161 */         return YamlProcessor.MatchStatus.NOT_FOUND;
/*     */       }
/* 163 */       for (String activeProfile : this.activeProfiles) {
/* 164 */         if (profiles.contains(activeProfile)) {
/* 165 */           return YamlProcessor.MatchStatus.FOUND;
/*     */         }
/*     */       }
/* 168 */       return YamlProcessor.MatchStatus.NOT_FOUND;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class EmptyProfilesMatcher
/*     */     extends SpringProfileDocumentMatcher.ProfilesMatcher
/*     */   {
/*     */     private EmptyProfilesMatcher()
/*     */     {
/* 179 */       super();
/*     */     }
/*     */     
/*     */     public YamlProcessor.MatchStatus doMatches(Set<String> springProfiles) {
/* 183 */       if (springProfiles.isEmpty()) {
/* 184 */         return YamlProcessor.MatchStatus.FOUND;
/*     */       }
/* 186 */       for (String profile : springProfiles) {
/* 187 */         if (!StringUtils.hasText(profile)) {
/* 188 */           return YamlProcessor.MatchStatus.FOUND;
/*     */         }
/*     */       }
/* 191 */       return YamlProcessor.MatchStatus.NOT_FOUND;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class SpringProperties
/*     */   {
/* 201 */     private List<String> profiles = new ArrayList();
/*     */     
/*     */     public List<String> getProfiles() {
/* 204 */       return this.profiles;
/*     */     }
/*     */     
/*     */     public void setProfiles(List<String> profiles) {
/* 208 */       this.profiles = profiles;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\yaml\SpringProfileDocumentMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */