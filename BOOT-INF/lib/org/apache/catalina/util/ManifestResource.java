/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ManifestResource
/*     */ {
/*     */   public static final int SYSTEM = 1;
/*     */   public static final int WAR = 2;
/*     */   public static final int APPLICATION = 3;
/*  40 */   private ArrayList<Extension> availableExtensions = null;
/*  41 */   private ArrayList<Extension> requiredExtensions = null;
/*     */   
/*     */   private final String resourceName;
/*     */   private final int resourceType;
/*     */   
/*     */   public ManifestResource(String resourceName, Manifest manifest, int resourceType)
/*     */   {
/*  48 */     this.resourceName = resourceName;
/*  49 */     this.resourceType = resourceType;
/*  50 */     processManifest(manifest);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getResourceName()
/*     */   {
/*  59 */     return this.resourceName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Extension> getAvailableExtensions()
/*     */   {
/*  68 */     return this.availableExtensions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Extension> getRequiredExtensions()
/*     */   {
/*  77 */     return this.requiredExtensions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getAvailableExtensionCount()
/*     */   {
/*  88 */     return this.availableExtensions != null ? this.availableExtensions.size() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredExtensionCount()
/*     */   {
/*  97 */     return this.requiredExtensions != null ? this.requiredExtensions.size() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFulfilled()
/*     */   {
/* 107 */     if (this.requiredExtensions == null) {
/* 108 */       return true;
/*     */     }
/* 110 */     Iterator<Extension> it = this.requiredExtensions.iterator();
/* 111 */     while (it.hasNext()) {
/* 112 */       Extension ext = (Extension)it.next();
/* 113 */       if (!ext.isFulfilled()) return false;
/*     */     }
/* 115 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 121 */     StringBuilder sb = new StringBuilder("ManifestResource[");
/* 122 */     sb.append(this.resourceName);
/*     */     
/* 124 */     sb.append(", isFulfilled=");
/* 125 */     sb.append(isFulfilled() + "");
/* 126 */     sb.append(", requiredExtensionCount =");
/* 127 */     sb.append(getRequiredExtensionCount());
/* 128 */     sb.append(", availableExtensionCount=");
/* 129 */     sb.append(getAvailableExtensionCount());
/* 130 */     switch (this.resourceType) {
/* 131 */     case 1:  sb.append(", resourceType=SYSTEM"); break;
/* 132 */     case 2:  sb.append(", resourceType=WAR"); break;
/* 133 */     case 3:  sb.append(", resourceType=APPLICATION");
/*     */     }
/* 135 */     sb.append("]");
/* 136 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void processManifest(Manifest manifest)
/*     */   {
/* 143 */     this.availableExtensions = getAvailableExtensions(manifest);
/* 144 */     this.requiredExtensions = getRequiredExtensions(manifest);
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
/*     */   private ArrayList<Extension> getRequiredExtensions(Manifest manifest)
/*     */   {
/* 159 */     Attributes attributes = manifest.getMainAttributes();
/* 160 */     String names = attributes.getValue("Extension-List");
/* 161 */     if (names == null) {
/* 162 */       return null;
/*     */     }
/* 164 */     ArrayList<Extension> extensionList = new ArrayList();
/* 165 */     names = names + " ";
/*     */     
/*     */     for (;;)
/*     */     {
/* 169 */       int space = names.indexOf(' ');
/* 170 */       if (space < 0)
/*     */         break;
/* 172 */       String name = names.substring(0, space).trim();
/* 173 */       names = names.substring(space + 1);
/*     */       
/*     */ 
/* 176 */       String value = attributes.getValue(name + "-Extension-Name");
/* 177 */       if (value != null)
/*     */       {
/* 179 */         Extension extension = new Extension();
/* 180 */         extension.setExtensionName(value);
/* 181 */         extension
/* 182 */           .setImplementationURL(attributes.getValue(name + "-Implementation-URL"));
/* 183 */         extension
/* 184 */           .setImplementationVendorId(attributes.getValue(name + "-Implementation-Vendor-Id"));
/* 185 */         String version = attributes.getValue(name + "-Implementation-Version");
/* 186 */         extension.setImplementationVersion(version);
/* 187 */         extension
/* 188 */           .setSpecificationVersion(attributes.getValue(name + "-Specification-Version"));
/* 189 */         extensionList.add(extension);
/*     */       } }
/* 191 */     return extensionList;
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
/*     */   private ArrayList<Extension> getAvailableExtensions(Manifest manifest)
/*     */   {
/* 206 */     Attributes attributes = manifest.getMainAttributes();
/* 207 */     String name = attributes.getValue("Extension-Name");
/* 208 */     if (name == null) {
/* 209 */       return null;
/*     */     }
/* 211 */     ArrayList<Extension> extensionList = new ArrayList();
/*     */     
/* 213 */     Extension extension = new Extension();
/* 214 */     extension.setExtensionName(name);
/* 215 */     extension.setImplementationURL(attributes
/* 216 */       .getValue("Implementation-URL"));
/* 217 */     extension.setImplementationVendor(attributes
/* 218 */       .getValue("Implementation-Vendor"));
/* 219 */     extension.setImplementationVendorId(attributes
/* 220 */       .getValue("Implementation-Vendor-Id"));
/* 221 */     extension.setImplementationVersion(attributes
/* 222 */       .getValue("Implementation-Version"));
/* 223 */     extension.setSpecificationVersion(attributes
/* 224 */       .getValue("Specification-Version"));
/*     */     
/* 226 */     extensionList.add(extension);
/*     */     
/* 228 */     return extensionList;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\ManifestResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */