/*     */ package org.apache.tomcat.util.net.openssl.ciphers;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class OpenSSLCipherConfigurationParser
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(OpenSSLCipherConfigurationParser.class);
/*     */   
/*  44 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.net.jsse.res");
/*     */   
/*  46 */   private static boolean initialized = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String SEPARATOR = ":|,| ";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String EXCLUDE = "!";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String DELETE = "-";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String TO_END = "+";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String AND = "+";
/*     */   
/*     */ 
/*     */ 
/*  73 */   private static final Map<String, List<Cipher>> aliases = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String eNULL = "eNULL";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aNULL = "aNULL";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String HIGH = "HIGH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String MEDIUM = "MEDIUM";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String LOW = "LOW";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String EXPORT = "EXPORT";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String EXPORT40 = "EXPORT40";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String EXPORT56 = "EXPORT56";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kRSA = "kRSA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aRSA = "aRSA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String RSA = "RSA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kEDH = "kEDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kDHE = "kDHE";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String EDH = "EDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String DHE = "DHE";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kDHr = "kDHr";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kDHd = "kDHd";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kDH = "kDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kECDHr = "kECDHr";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kECDHe = "kECDHe";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kECDH = "kECDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kEECDH = "kEECDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String EECDH = "EECDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ECDH = "ECDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kECDHE = "kECDHE";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ECDHE = "ECDHE";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String EECDHE = "EECDHE";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String AECDH = "AECDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String DSS = "DSS";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aDSS = "aDSS";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aDH = "aDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aECDH = "aECDH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aECDSA = "aECDSA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ECDSA = "ECDSA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kFZA = "kFZA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aFZA = "aFZA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String eFZA = "eFZA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String FZA = "FZA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String DH = "DH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ADH = "ADH";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String AES128 = "AES128";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String AES256 = "AES256";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String AES = "AES";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String AESGCM = "AESGCM";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String AESCCM = "AESCCM";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String AESCCM8 = "AESCCM8";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ARIA128 = "ARIA128";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ARIA256 = "ARIA256";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ARIA = "ARIA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String CAMELLIA128 = "CAMELLIA128";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String CAMELLIA256 = "CAMELLIA256";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String CAMELLIA = "CAMELLIA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String CHACHA20 = "CHACHA20";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String TRIPLE_DES = "3DES";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String DES = "DES";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String RC4 = "RC4";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String RC2 = "RC2";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String IDEA = "IDEA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String SEED = "SEED";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String MD5 = "MD5";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String SHA1 = "SHA1";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String SHA = "SHA";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String SHA256 = "SHA256";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String SHA384 = "SHA384";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String KRB5 = "KRB5";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aGOST = "aGOST";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aGOST01 = "aGOST01";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aGOST94 = "aGOST94";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kGOST = "kGOST";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String GOST94 = "GOST94";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String GOST89MAC = "GOST89MAC";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String aSRP = "aSRP";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String kSRP = "kSRP";
/*     */   
/*     */ 
/*     */   private static final String SRP = "SRP";
/*     */   
/*     */ 
/*     */   private static final String PSK = "PSK";
/*     */   
/*     */ 
/*     */   private static final String aPSK = "aPSK";
/*     */   
/*     */ 
/*     */   private static final String kPSK = "kPSK";
/*     */   
/*     */ 
/*     */   private static final String kRSAPSK = "kRSAPSK";
/*     */   
/*     */ 
/*     */   private static final String kECDHEPSK = "kECDHEPSK";
/*     */   
/*     */ 
/*     */   private static final String kDHEPSK = "kDHEPSK";
/*     */   
/*     */ 
/*     */   private static final String DEFAULT = "DEFAULT";
/*     */   
/*     */ 
/*     */   private static final String COMPLEMENTOFDEFAULT = "COMPLEMENTOFDEFAULT";
/*     */   
/*     */ 
/*     */   private static final String ALL = "ALL";
/*     */   
/*     */ 
/*     */   private static final String COMPLEMENTOFALL = "COMPLEMENTOFALL";
/*     */   
/*     */ 
/* 401 */   private static final Map<String, String> jsseToOpenSSL = new HashMap();
/*     */   
/*     */   private static final void init() { Cipher cipher;
/*     */     String openSSlAltName;
/* 405 */     for (cipher : ) {
/* 406 */       String alias = cipher.getOpenSSLAlias();
/* 407 */       if (aliases.containsKey(alias)) {
/* 408 */         ((List)aliases.get(alias)).add(cipher);
/*     */       } else {
/* 410 */         list = new ArrayList();
/* 411 */         list.add(cipher);
/* 412 */         aliases.put(alias, list);
/*     */       }
/* 414 */       aliases.put(cipher.name(), Collections.singletonList(cipher));
/*     */       
/* 416 */       for (List<Cipher> list = cipher.getOpenSSLAltNames().iterator(); list.hasNext();) { openSSlAltName = (String)list.next();
/* 417 */         if (aliases.containsKey(openSSlAltName)) {
/* 418 */           ((List)aliases.get(openSSlAltName)).add(cipher);
/*     */         } else {
/* 420 */           List<Cipher> list = new ArrayList();
/* 421 */           list.add(cipher);
/* 422 */           aliases.put(openSSlAltName, list);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 427 */       jsseToOpenSSL.put(cipher.name(), cipher.getOpenSSLAlias());
/* 428 */       Set<String> jsseNames = cipher.getJsseNames();
/* 429 */       for (String jsseName : jsseNames) {
/* 430 */         jsseToOpenSSL.put(jsseName, cipher.getOpenSSLAlias());
/*     */       }
/*     */     }
/* 433 */     Object allCiphersList = Arrays.asList(Cipher.values());
/* 434 */     Collections.reverse((List)allCiphersList);
/* 435 */     Object allCiphers = defaultSort(new LinkedHashSet((Collection)allCiphersList));
/* 436 */     addListAlias("eNULL", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.eNULL)));
/* 437 */     Object all = new LinkedHashSet((Collection)allCiphers);
/* 438 */     remove((Set)all, "eNULL");
/* 439 */     addListAlias("ALL", (Set)all);
/* 440 */     addListAlias("HIGH", filterByEncryptionLevel((Set)allCiphers, Collections.singleton(EncryptionLevel.HIGH)));
/* 441 */     addListAlias("MEDIUM", filterByEncryptionLevel((Set)allCiphers, Collections.singleton(EncryptionLevel.MEDIUM)));
/* 442 */     addListAlias("LOW", filterByEncryptionLevel((Set)allCiphers, Collections.singleton(EncryptionLevel.LOW)));
/* 443 */     addListAlias("EXPORT", filterByEncryptionLevel((Set)allCiphers, new HashSet(Arrays.asList(new EncryptionLevel[] { EncryptionLevel.EXP40, EncryptionLevel.EXP56 }))));
/* 444 */     aliases.put("EXP", aliases.get("EXPORT"));
/* 445 */     addListAlias("EXPORT40", filterByEncryptionLevel((Set)allCiphers, Collections.singleton(EncryptionLevel.EXP40)));
/* 446 */     addListAlias("EXPORT56", filterByEncryptionLevel((Set)allCiphers, Collections.singleton(EncryptionLevel.EXP56)));
/* 447 */     aliases.put("NULL", aliases.get("eNULL"));
/* 448 */     aliases.put("COMPLEMENTOFALL", aliases.get("eNULL"));
/* 449 */     addListAlias("aNULL", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.aNULL)));
/* 450 */     addListAlias("kRSA", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.RSA)));
/* 451 */     addListAlias("aRSA", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.RSA)));
/*     */     
/* 453 */     aliases.put("RSA", aliases.get("kRSA"));
/* 454 */     addListAlias("kEDH", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EDH)));
/* 455 */     addListAlias("kDHE", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EDH)));
/* 456 */     Set<Cipher> edh = filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EDH));
/* 457 */     edh.removeAll(filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.aNULL)));
/* 458 */     addListAlias("EDH", edh);
/* 459 */     addListAlias("DHE", edh);
/* 460 */     addListAlias("kDHr", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.DHr)));
/* 461 */     addListAlias("kDHd", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.DHd)));
/* 462 */     addListAlias("kDH", filterByKeyExchange((Set)allCiphers, new HashSet(Arrays.asList(new KeyExchange[] { KeyExchange.DHr, KeyExchange.DHd }))));
/*     */     
/* 464 */     addListAlias("kECDHr", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.ECDHr)));
/* 465 */     addListAlias("kECDHe", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.ECDHe)));
/* 466 */     addListAlias("kECDH", filterByKeyExchange((Set)allCiphers, new HashSet(Arrays.asList(new KeyExchange[] { KeyExchange.ECDHe, KeyExchange.ECDHr }))));
/* 467 */     addListAlias("ECDH", filterByKeyExchange((Set)allCiphers, new HashSet(Arrays.asList(new KeyExchange[] { KeyExchange.ECDHe, KeyExchange.ECDHr, KeyExchange.EECDH }))));
/* 468 */     addListAlias("kECDHE", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EECDH)));
/*     */     
/* 470 */     Set<Cipher> ecdhe = filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EECDH));
/* 471 */     remove(ecdhe, "aNULL");
/* 472 */     addListAlias("ECDHE", ecdhe);
/*     */     
/* 474 */     addListAlias("kEECDH", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EECDH)));
/* 475 */     aliases.put("EECDHE", aliases.get("kEECDH"));
/* 476 */     Set<Cipher> eecdh = filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EECDH));
/* 477 */     eecdh.removeAll(filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.aNULL)));
/* 478 */     addListAlias("EECDH", eecdh);
/* 479 */     addListAlias("aDSS", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.DSS)));
/* 480 */     aliases.put("DSS", aliases.get("aDSS"));
/* 481 */     addListAlias("aDH", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.DH)));
/* 482 */     Set<Cipher> aecdh = filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EECDH));
/* 483 */     addListAlias("AECDH", filterByAuthentication(aecdh, Collections.singleton(Authentication.aNULL)));
/* 484 */     addListAlias("aECDH", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.ECDH)));
/* 485 */     addListAlias("ECDSA", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.ECDSA)));
/* 486 */     aliases.put("aECDSA", aliases.get("ECDSA"));
/* 487 */     addListAlias("kFZA", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.FZA)));
/* 488 */     addListAlias("aFZA", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.FZA)));
/* 489 */     addListAlias("eFZA", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.FZA)));
/* 490 */     addListAlias("FZA", filter((Set)allCiphers, null, Collections.singleton(KeyExchange.FZA), Collections.singleton(Authentication.FZA), Collections.singleton(Encryption.FZA), null, null));
/* 491 */     addListAlias("TLSv1.2", filterByProtocol((Set)allCiphers, Collections.singleton(Protocol.TLSv1_2)));
/* 492 */     addListAlias("TLSv1.0", filterByProtocol((Set)allCiphers, Collections.singleton(Protocol.TLSv1)));
/* 493 */     addListAlias("SSLv3", filterByProtocol((Set)allCiphers, Collections.singleton(Protocol.SSLv3)));
/* 494 */     aliases.put("TLSv1", aliases.get("TLSv1.0"));
/* 495 */     addListAlias("SSLv2", filterByProtocol((Set)allCiphers, Collections.singleton(Protocol.SSLv2)));
/* 496 */     addListAlias("DH", filterByKeyExchange((Set)allCiphers, new HashSet(Arrays.asList(new KeyExchange[] { KeyExchange.DHr, KeyExchange.DHd, KeyExchange.EDH }))));
/* 497 */     Set<Cipher> adh = filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.EDH));
/* 498 */     adh.retainAll(filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.aNULL)));
/* 499 */     addListAlias("ADH", adh);
/* 500 */     addListAlias("AES128", filterByEncryption((Set)allCiphers, new HashSet(Arrays.asList(new Encryption[] { Encryption.AES128, Encryption.AES128CCM, Encryption.AES128CCM8, Encryption.AES128GCM }))));
/* 501 */     addListAlias("AES256", filterByEncryption((Set)allCiphers, new HashSet(Arrays.asList(new Encryption[] { Encryption.AES256, Encryption.AES256CCM, Encryption.AES256CCM8, Encryption.AES256GCM }))));
/* 502 */     addListAlias("AES", filterByEncryption((Set)allCiphers, new HashSet(Arrays.asList(new Encryption[] { Encryption.AES128, Encryption.AES128CCM, Encryption.AES128CCM8, Encryption.AES128GCM, Encryption.AES256, Encryption.AES256CCM, Encryption.AES256CCM8, Encryption.AES256GCM }))));
/* 503 */     addListAlias("ARIA128", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.ARIA128GCM)));
/* 504 */     addListAlias("ARIA256", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.ARIA256GCM)));
/* 505 */     addListAlias("ARIA", filterByEncryption((Set)allCiphers, new HashSet(Arrays.asList(new Encryption[] { Encryption.ARIA128GCM, Encryption.ARIA256GCM }))));
/* 506 */     addListAlias("AESGCM", filterByEncryption((Set)allCiphers, new HashSet(Arrays.asList(new Encryption[] { Encryption.AES128GCM, Encryption.AES256GCM }))));
/* 507 */     addListAlias("AESCCM", filterByEncryption((Set)allCiphers, new HashSet(Arrays.asList(new Encryption[] { Encryption.AES128CCM, Encryption.AES128CCM8, Encryption.AES256CCM, Encryption.AES256CCM8 }))));
/* 508 */     addListAlias("AESCCM8", filterByEncryption((Set)allCiphers, new HashSet(Arrays.asList(new Encryption[] { Encryption.AES128CCM8, Encryption.AES256CCM8 }))));
/* 509 */     addListAlias("CAMELLIA", filterByEncryption((Set)allCiphers, new HashSet(Arrays.asList(new Encryption[] { Encryption.CAMELLIA128, Encryption.CAMELLIA256 }))));
/* 510 */     addListAlias("CAMELLIA128", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.CAMELLIA128)));
/* 511 */     addListAlias("CAMELLIA256", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.CAMELLIA256)));
/* 512 */     addListAlias("CHACHA20", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.CHACHA20POLY1305)));
/* 513 */     addListAlias("3DES", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.TRIPLE_DES)));
/* 514 */     addListAlias("DES", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.DES)));
/* 515 */     addListAlias("RC4", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.RC4)));
/* 516 */     addListAlias("RC2", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.RC2)));
/* 517 */     addListAlias("IDEA", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.IDEA)));
/* 518 */     addListAlias("SEED", filterByEncryption((Set)allCiphers, Collections.singleton(Encryption.SEED)));
/* 519 */     addListAlias("MD5", filterByMessageDigest((Set)allCiphers, Collections.singleton(MessageDigest.MD5)));
/* 520 */     addListAlias("SHA1", filterByMessageDigest((Set)allCiphers, Collections.singleton(MessageDigest.SHA1)));
/* 521 */     aliases.put("SHA", aliases.get("SHA1"));
/* 522 */     addListAlias("SHA256", filterByMessageDigest((Set)allCiphers, Collections.singleton(MessageDigest.SHA256)));
/* 523 */     addListAlias("SHA384", filterByMessageDigest((Set)allCiphers, Collections.singleton(MessageDigest.SHA384)));
/* 524 */     addListAlias("aGOST", filterByAuthentication((Set)allCiphers, new HashSet(Arrays.asList(new Authentication[] { Authentication.GOST01, Authentication.GOST94 }))));
/* 525 */     addListAlias("aGOST01", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.GOST01)));
/* 526 */     addListAlias("aGOST94", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.GOST94)));
/* 527 */     addListAlias("kGOST", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.GOST)));
/* 528 */     addListAlias("GOST94", filterByMessageDigest((Set)allCiphers, Collections.singleton(MessageDigest.GOST94)));
/* 529 */     addListAlias("GOST89MAC", filterByMessageDigest((Set)allCiphers, Collections.singleton(MessageDigest.GOST89MAC)));
/* 530 */     addListAlias("PSK", filter((Set)allCiphers, null, new HashSet(Arrays.asList(new KeyExchange[] { KeyExchange.PSK, KeyExchange.RSAPSK, KeyExchange.DHEPSK, KeyExchange.ECDHEPSK })), Collections.singleton(Authentication.PSK), null, null, null));
/* 531 */     addListAlias("aPSK", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.PSK)));
/* 532 */     addListAlias("kPSK", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.PSK)));
/* 533 */     addListAlias("kRSAPSK", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.RSAPSK)));
/* 534 */     addListAlias("kECDHEPSK", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.ECDHEPSK)));
/* 535 */     addListAlias("kDHEPSK", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.DHEPSK)));
/* 536 */     addListAlias("KRB5", filter((Set)allCiphers, null, Collections.singleton(KeyExchange.KRB5), Collections.singleton(Authentication.KRB5), null, null, null));
/* 537 */     addListAlias("aSRP", filterByAuthentication((Set)allCiphers, Collections.singleton(Authentication.SRP)));
/* 538 */     addListAlias("kSRP", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.SRP)));
/* 539 */     addListAlias("SRP", filterByKeyExchange((Set)allCiphers, Collections.singleton(KeyExchange.SRP)));
/* 540 */     initialized = true;
/*     */     
/* 542 */     addListAlias("DEFAULT", parse("ALL:!EXPORT:!eNULL:!aNULL:!SSLv2:!DES:!RC2:!RC4:!DSS:!SEED:!IDEA:!CAMELLIA:!AESCCM:!3DES:!ARIA"));
/*     */     
/* 544 */     LinkedHashSet<Cipher> complementOfDefault = filterByKeyExchange((Set)all, new HashSet(Arrays.asList(new KeyExchange[] { KeyExchange.EDH, KeyExchange.EECDH })));
/* 545 */     complementOfDefault = filterByAuthentication(complementOfDefault, Collections.singleton(Authentication.aNULL));
/* 546 */     complementOfDefault.removeAll((Collection)aliases.get("eNULL"));
/* 547 */     complementOfDefault.addAll((Collection)aliases.get("SSLv2"));
/* 548 */     complementOfDefault.addAll((Collection)aliases.get("EXPORT"));
/* 549 */     complementOfDefault.addAll((Collection)aliases.get("DES"));
/* 550 */     complementOfDefault.addAll((Collection)aliases.get("3DES"));
/* 551 */     complementOfDefault.addAll((Collection)aliases.get("RC2"));
/* 552 */     complementOfDefault.addAll((Collection)aliases.get("RC4"));
/* 553 */     complementOfDefault.addAll((Collection)aliases.get("aDSS"));
/* 554 */     complementOfDefault.addAll((Collection)aliases.get("SEED"));
/* 555 */     complementOfDefault.addAll((Collection)aliases.get("IDEA"));
/* 556 */     complementOfDefault.addAll((Collection)aliases.get("CAMELLIA"));
/* 557 */     complementOfDefault.addAll((Collection)aliases.get("AESCCM"));
/* 558 */     complementOfDefault.addAll((Collection)aliases.get("ARIA"));
/* 559 */     defaultSort(complementOfDefault);
/* 560 */     addListAlias("COMPLEMENTOFDEFAULT", complementOfDefault);
/*     */   }
/*     */   
/*     */   static void addListAlias(String alias, Set<Cipher> ciphers) {
/* 564 */     aliases.put(alias, new ArrayList(ciphers));
/*     */   }
/*     */   
/*     */   static void moveToEnd(LinkedHashSet<Cipher> ciphers, String alias) {
/* 568 */     moveToEnd(ciphers, (Collection)aliases.get(alias));
/*     */   }
/*     */   
/*     */   static void moveToEnd(LinkedHashSet<Cipher> ciphers, Collection<Cipher> toBeMovedCiphers) {
/* 572 */     List<Cipher> movedCiphers = new ArrayList(toBeMovedCiphers);
/* 573 */     movedCiphers.retainAll(ciphers);
/* 574 */     ciphers.removeAll(movedCiphers);
/* 575 */     ciphers.addAll(movedCiphers);
/*     */   }
/*     */   
/*     */   static void moveToStart(LinkedHashSet<Cipher> ciphers, Collection<Cipher> toBeMovedCiphers) {
/* 579 */     List<Cipher> movedCiphers = new ArrayList(toBeMovedCiphers);
/* 580 */     List<Cipher> originalCiphers = new ArrayList(ciphers);
/* 581 */     movedCiphers.retainAll(ciphers);
/* 582 */     ciphers.clear();
/* 583 */     ciphers.addAll(movedCiphers);
/* 584 */     ciphers.addAll(originalCiphers);
/*     */   }
/*     */   
/*     */   static void add(LinkedHashSet<Cipher> ciphers, String alias) {
/* 588 */     ciphers.addAll((Collection)aliases.get(alias));
/*     */   }
/*     */   
/*     */   static void remove(Set<Cipher> ciphers, String alias) {
/* 592 */     ciphers.removeAll((Collection)aliases.get(alias));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static LinkedHashSet<Cipher> strengthSort(LinkedHashSet<Cipher> ciphers)
/*     */   {
/* 601 */     Set<Integer> keySizes = new HashSet();
/* 602 */     for (Cipher cipher : ciphers) {
/* 603 */       keySizes.add(Integer.valueOf(cipher.getStrength_bits()));
/*     */     }
/* 605 */     Object strength_bits = new ArrayList(keySizes);
/* 606 */     Collections.sort((List)strength_bits);
/* 607 */     Collections.reverse((List)strength_bits);
/* 608 */     LinkedHashSet<Cipher> result = new LinkedHashSet(ciphers);
/* 609 */     for (Iterator localIterator2 = ((List)strength_bits).iterator(); localIterator2.hasNext();) { int strength = ((Integer)localIterator2.next()).intValue();
/* 610 */       moveToEnd(result, filterByStrengthBits(ciphers, strength));
/*     */     }
/* 612 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static LinkedHashSet<Cipher> defaultSort(LinkedHashSet<Cipher> ciphers)
/*     */   {
/* 620 */     LinkedHashSet<Cipher> result = new LinkedHashSet(ciphers.size());
/* 621 */     LinkedHashSet<Cipher> ecdh = new LinkedHashSet(ciphers.size());
/*     */     
/*     */ 
/* 624 */     ecdh.addAll(filterByKeyExchange(ciphers, Collections.singleton(KeyExchange.EECDH)));
/*     */     
/*     */ 
/* 627 */     Set<Encryption> aes = new HashSet(Arrays.asList(new Encryption[] { Encryption.AES128, Encryption.AES128CCM, Encryption.AES128CCM8, Encryption.AES128GCM, Encryption.AES256, Encryption.AES256CCM, Encryption.AES256CCM8, Encryption.AES256GCM }));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 632 */     result.addAll(filterByEncryption(ecdh, aes));
/* 633 */     result.addAll(filterByEncryption(ciphers, aes));
/*     */     
/*     */ 
/* 636 */     result.addAll(ecdh);
/* 637 */     result.addAll(ciphers);
/*     */     
/*     */ 
/* 640 */     moveToEnd(result, filterByMessageDigest(result, Collections.singleton(MessageDigest.MD5)));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 645 */     moveToEnd(result, filterByAuthentication(result, Collections.singleton(Authentication.aNULL)));
/*     */     
/*     */ 
/* 648 */     moveToEnd(result, filterByAuthentication(result, Collections.singleton(Authentication.ECDH)));
/* 649 */     moveToEnd(result, filterByKeyExchange(result, Collections.singleton(KeyExchange.RSA)));
/* 650 */     moveToEnd(result, filterByKeyExchange(result, Collections.singleton(KeyExchange.PSK)));
/*     */     
/*     */ 
/* 653 */     moveToEnd(result, filterByEncryption(result, Collections.singleton(Encryption.RC4)));
/* 654 */     return strengthSort(result);
/*     */   }
/*     */   
/*     */   static Set<Cipher> filterByStrengthBits(Set<Cipher> ciphers, int strength_bits) {
/* 658 */     Set<Cipher> result = new LinkedHashSet(ciphers.size());
/* 659 */     for (Cipher cipher : ciphers) {
/* 660 */       if (cipher.getStrength_bits() == strength_bits) {
/* 661 */         result.add(cipher);
/*     */       }
/*     */     }
/* 664 */     return result;
/*     */   }
/*     */   
/*     */   static Set<Cipher> filterByProtocol(Set<Cipher> ciphers, Set<Protocol> protocol) {
/* 668 */     return filter(ciphers, protocol, null, null, null, null, null);
/*     */   }
/*     */   
/*     */   static LinkedHashSet<Cipher> filterByKeyExchange(Set<Cipher> ciphers, Set<KeyExchange> kx) {
/* 672 */     return filter(ciphers, null, kx, null, null, null, null);
/*     */   }
/*     */   
/*     */   static LinkedHashSet<Cipher> filterByAuthentication(Set<Cipher> ciphers, Set<Authentication> au) {
/* 676 */     return filter(ciphers, null, null, au, null, null, null);
/*     */   }
/*     */   
/*     */   static Set<Cipher> filterByEncryption(Set<Cipher> ciphers, Set<Encryption> enc) {
/* 680 */     return filter(ciphers, null, null, null, enc, null, null);
/*     */   }
/*     */   
/*     */   static Set<Cipher> filterByEncryptionLevel(Set<Cipher> ciphers, Set<EncryptionLevel> level) {
/* 684 */     return filter(ciphers, null, null, null, null, level, null);
/*     */   }
/*     */   
/*     */   static Set<Cipher> filterByMessageDigest(Set<Cipher> ciphers, Set<MessageDigest> mac) {
/* 688 */     return filter(ciphers, null, null, null, null, null, mac);
/*     */   }
/*     */   
/*     */   static LinkedHashSet<Cipher> filter(Set<Cipher> ciphers, Set<Protocol> protocol, Set<KeyExchange> kx, Set<Authentication> au, Set<Encryption> enc, Set<EncryptionLevel> level, Set<MessageDigest> mac)
/*     */   {
/* 693 */     LinkedHashSet<Cipher> result = new LinkedHashSet(ciphers.size());
/* 694 */     for (Cipher cipher : ciphers) {
/* 695 */       if ((protocol != null) && (protocol.contains(cipher.getProtocol()))) {
/* 696 */         result.add(cipher);
/*     */       }
/* 698 */       if ((kx != null) && (kx.contains(cipher.getKx()))) {
/* 699 */         result.add(cipher);
/*     */       }
/* 701 */       if ((au != null) && (au.contains(cipher.getAu()))) {
/* 702 */         result.add(cipher);
/*     */       }
/* 704 */       if ((enc != null) && (enc.contains(cipher.getEnc()))) {
/* 705 */         result.add(cipher);
/*     */       }
/* 707 */       if ((level != null) && (level.contains(cipher.getLevel()))) {
/* 708 */         result.add(cipher);
/*     */       }
/* 710 */       if ((mac != null) && (mac.contains(cipher.getMac()))) {
/* 711 */         result.add(cipher);
/*     */       }
/*     */     }
/* 714 */     return result;
/*     */   }
/*     */   
/*     */   public static LinkedHashSet<Cipher> parse(String expression) {
/* 718 */     if (!initialized) {
/* 719 */       init();
/*     */     }
/* 721 */     String[] elements = expression.split(":|,| ");
/* 722 */     LinkedHashSet<Cipher> ciphers = new LinkedHashSet();
/* 723 */     Set<Cipher> removedCiphers = new HashSet();
/* 724 */     for (String element : elements)
/* 725 */       if (element.startsWith("-")) {
/* 726 */         String alias = element.substring(1);
/* 727 */         if (aliases.containsKey(alias)) {
/* 728 */           remove(ciphers, alias);
/*     */         }
/* 730 */       } else if (element.startsWith("!")) {
/* 731 */         String alias = element.substring(1);
/* 732 */         if (aliases.containsKey(alias)) {
/* 733 */           removedCiphers.addAll((Collection)aliases.get(alias));
/*     */         } else {
/* 735 */           log.warn(sm.getString("jsse.openssl.unknownElement", new Object[] { alias }));
/*     */         }
/* 737 */       } else if (element.startsWith("+")) {
/* 738 */         String alias = element.substring(1);
/* 739 */         if (aliases.containsKey(alias))
/* 740 */           moveToEnd(ciphers, alias);
/*     */       } else {
/* 742 */         if ("@STRENGTH".equals(element)) {
/* 743 */           strengthSort(ciphers);
/* 744 */           break; }
/* 745 */         if (aliases.containsKey(element)) {
/* 746 */           add(ciphers, element);
/* 747 */         } else if (element.contains("+")) {
/* 748 */           String[] intersections = element.split("\\+");
/* 749 */           if ((intersections.length > 0) && (aliases.containsKey(intersections[0]))) {
/* 750 */             List<Cipher> result = new ArrayList((Collection)aliases.get(intersections[0]));
/* 751 */             for (int i = 1; i < intersections.length; i++) {
/* 752 */               if (aliases.containsKey(intersections[i])) {
/* 753 */                 result.retainAll((Collection)aliases.get(intersections[i]));
/*     */               }
/*     */             }
/* 756 */             ciphers.addAll(result);
/*     */           }
/*     */         }
/*     */       }
/* 760 */     ciphers.removeAll(removedCiphers);
/* 761 */     return ciphers;
/*     */   }
/*     */   
/*     */   public static List<String> convertForJSSE(Collection<Cipher> ciphers) {
/* 765 */     List<String> result = new ArrayList(ciphers.size());
/* 766 */     for (Cipher cipher : ciphers) {
/* 767 */       result.addAll(cipher.getJsseNames());
/*     */     }
/* 769 */     if (log.isDebugEnabled()) {
/* 770 */       log.debug(sm.getString("jsse.openssl.effectiveCiphers", new Object[] { displayResult(ciphers, true, ",") }));
/*     */     }
/* 772 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> parseExpression(String expression)
/*     */   {
/* 783 */     return convertForJSSE(parse(expression));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String jsseToOpenSSL(String jsseCipherName)
/*     */   {
/* 795 */     if (!initialized) {
/* 796 */       init();
/*     */     }
/* 798 */     return (String)jsseToOpenSSL.get(jsseCipherName);
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
/*     */   public static String openSSLToJsse(String opensslCipherName)
/*     */   {
/* 811 */     if (!initialized) {
/* 812 */       init();
/*     */     }
/* 814 */     List<Cipher> ciphers = (List)aliases.get(opensslCipherName);
/* 815 */     if ((ciphers == null) || (ciphers.size() != 1))
/*     */     {
/* 817 */       return null;
/*     */     }
/* 819 */     Cipher cipher = (Cipher)ciphers.get(0);
/*     */     
/* 821 */     return (String)cipher.getJsseNames().iterator().next();
/*     */   }
/*     */   
/*     */   static String displayResult(Collection<Cipher> ciphers, boolean useJSSEFormat, String separator)
/*     */   {
/* 826 */     if (ciphers.isEmpty()) {
/* 827 */       return "";
/*     */     }
/* 829 */     StringBuilder builder = new StringBuilder(ciphers.size() * 16);
/* 830 */     for (Cipher cipher : ciphers) {
/* 831 */       if (useJSSEFormat) {
/* 832 */         for (String name : cipher.getJsseNames()) {
/* 833 */           builder.append(name);
/* 834 */           builder.append(separator);
/*     */         }
/*     */       } else {
/* 837 */         builder.append(cipher.getOpenSSLAlias());
/*     */       }
/* 839 */       builder.append(separator);
/*     */     }
/* 841 */     return builder.toString().substring(0, builder.length() - 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\ciphers\OpenSSLCipherConfigurationParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */