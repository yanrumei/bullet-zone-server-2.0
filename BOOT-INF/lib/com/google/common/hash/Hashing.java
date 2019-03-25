/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.security.Key;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Checksum;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Hashing
/*     */ {
/*     */   public static HashFunction goodFastHash(int minimumBits)
/*     */   {
/*  65 */     int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
/*     */     
/*  67 */     if (bits == 32) {
/*  68 */       return Murmur3_32Holder.GOOD_FAST_HASH_FUNCTION_32;
/*     */     }
/*  70 */     if (bits <= 128) {
/*  71 */       return Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
/*     */     }
/*     */     
/*     */ 
/*  75 */     int hashFunctionsNeeded = (bits + 127) / 128;
/*  76 */     HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
/*  77 */     hashFunctions[0] = Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
/*  78 */     int seed = GOOD_FAST_HASH_SEED;
/*  79 */     for (int i = 1; i < hashFunctionsNeeded; i++) {
/*  80 */       seed += 1500450271;
/*  81 */       hashFunctions[i] = murmur3_128(seed);
/*     */     }
/*  83 */     return new ConcatenatedHashFunction(hashFunctions, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */   private static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction murmur3_32(int seed)
/*     */   {
/* 100 */     return new Murmur3_32HashFunction(seed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction murmur3_32()
/*     */   {
/* 111 */     return Murmur3_32Holder.MURMUR3_32;
/*     */   }
/*     */   
/*     */   private static class Murmur3_32Holder {
/* 115 */     static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
/*     */     
/*     */ 
/* 118 */     static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = Hashing.murmur3_32(Hashing.GOOD_FAST_HASH_SEED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction murmur3_128(int seed)
/*     */   {
/* 129 */     return new Murmur3_128HashFunction(seed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction murmur3_128()
/*     */   {
/* 140 */     return Murmur3_128Holder.MURMUR3_128;
/*     */   }
/*     */   
/*     */   private static class Murmur3_128Holder {
/* 144 */     static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
/*     */     
/*     */ 
/* 147 */     static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = Hashing.murmur3_128(Hashing.GOOD_FAST_HASH_SEED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction sipHash24()
/*     */   {
/* 157 */     return SipHash24Holder.SIP_HASH_24;
/*     */   }
/*     */   
/*     */   private static class SipHash24Holder {
/* 161 */     static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction sipHash24(long k0, long k1)
/*     */   {
/* 172 */     return new SipHashFunction(2, 4, k0, k1);
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
/*     */   @Deprecated
/*     */   public static HashFunction md5()
/*     */   {
/* 189 */     return Md5Holder.MD5;
/*     */   }
/*     */   
/*     */   private static class Md5Holder {
/* 193 */     static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
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
/*     */   @Deprecated
/*     */   public static HashFunction sha1()
/*     */   {
/* 210 */     return Sha1Holder.SHA_1;
/*     */   }
/*     */   
/*     */   private static class Sha1Holder {
/* 214 */     static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
/*     */   }
/*     */   
/*     */   public static HashFunction sha256()
/*     */   {
/* 219 */     return Sha256Holder.SHA_256;
/*     */   }
/*     */   
/*     */   private static class Sha256Holder {
/* 223 */     static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction sha384()
/*     */   {
/* 233 */     return Sha384Holder.SHA_384;
/*     */   }
/*     */   
/*     */   private static class Sha384Holder {
/* 237 */     static final HashFunction SHA_384 = new MessageDigestHashFunction("SHA-384", "Hashing.sha384()");
/*     */   }
/*     */   
/*     */ 
/*     */   public static HashFunction sha512()
/*     */   {
/* 243 */     return Sha512Holder.SHA_512;
/*     */   }
/*     */   
/*     */   private static class Sha512Holder {
/* 247 */     static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
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
/*     */   public static HashFunction hmacMd5(Key key)
/*     */   {
/* 261 */     return new MacHashFunction("HmacMD5", key, hmacToString("hmacMd5", key));
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
/*     */   public static HashFunction hmacMd5(byte[] key)
/*     */   {
/* 274 */     return hmacMd5(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacMD5"));
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
/*     */   public static HashFunction hmacSha1(Key key)
/*     */   {
/* 287 */     return new MacHashFunction("HmacSHA1", key, hmacToString("hmacSha1", key));
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
/*     */   public static HashFunction hmacSha1(byte[] key)
/*     */   {
/* 300 */     return hmacSha1(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA1"));
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
/*     */   public static HashFunction hmacSha256(Key key)
/*     */   {
/* 313 */     return new MacHashFunction("HmacSHA256", key, hmacToString("hmacSha256", key));
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
/*     */   public static HashFunction hmacSha256(byte[] key)
/*     */   {
/* 326 */     return hmacSha256(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA256"));
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
/*     */   public static HashFunction hmacSha512(Key key)
/*     */   {
/* 339 */     return new MacHashFunction("HmacSHA512", key, hmacToString("hmacSha512", key));
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
/*     */   public static HashFunction hmacSha512(byte[] key)
/*     */   {
/* 352 */     return hmacSha512(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA512"));
/*     */   }
/*     */   
/*     */   private static String hmacToString(String methodName, Key key) {
/* 356 */     return String.format("Hashing.%s(Key[algorithm=%s, format=%s])", new Object[] { methodName, key
/*     */     
/*     */ 
/* 359 */       .getAlgorithm(), key
/* 360 */       .getFormat() });
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
/*     */   public static HashFunction crc32c()
/*     */   {
/* 374 */     return Crc32cHolder.CRC_32_C;
/*     */   }
/*     */   
/*     */   private static final class Crc32cHolder {
/* 378 */     static final HashFunction CRC_32_C = new Crc32cHashFunction();
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
/*     */   public static HashFunction crc32()
/*     */   {
/* 394 */     return Crc32Holder.CRC_32;
/*     */   }
/*     */   
/*     */   private static class Crc32Holder {
/* 398 */     static final HashFunction CRC_32 = Hashing.checksumHashFunction(Hashing.ChecksumType.CRC_32, "Hashing.crc32()");
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
/*     */   public static HashFunction adler32()
/*     */   {
/* 414 */     return Adler32Holder.ADLER_32;
/*     */   }
/*     */   
/*     */   private static class Adler32Holder
/*     */   {
/* 419 */     static final HashFunction ADLER_32 = Hashing.checksumHashFunction(Hashing.ChecksumType.ADLER_32, "Hashing.adler32()");
/*     */   }
/*     */   
/*     */   private static HashFunction checksumHashFunction(ChecksumType type, String toString) {
/* 423 */     return new ChecksumHashFunction(type, type.bits, toString);
/*     */   }
/*     */   
/*     */   static abstract enum ChecksumType implements Supplier<Checksum> {
/* 427 */     CRC_32(32), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 433 */     ADLER_32(32);
/*     */     
/*     */ 
/*     */ 
/*     */     private final int bits;
/*     */     
/*     */ 
/*     */ 
/*     */     private ChecksumType(int bits)
/*     */     {
/* 443 */       this.bits = bits;
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
/*     */     public abstract Checksum get();
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
/*     */   public static HashFunction farmHashFingerprint64()
/*     */   {
/* 467 */     return FarmHashFingerprint64Holder.FARMHASH_FINGERPRINT_64;
/*     */   }
/*     */   
/*     */   private static class FarmHashFingerprint64Holder {
/* 471 */     static final HashFunction FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();
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
/*     */   public static int consistentHash(HashCode hashCode, int buckets)
/*     */   {
/* 506 */     return consistentHash(hashCode.padToLong(), buckets);
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
/*     */   public static int consistentHash(long input, int buckets)
/*     */   {
/* 541 */     Preconditions.checkArgument(buckets > 0, "buckets must be positive: %s", buckets);
/* 542 */     LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
/* 543 */     int candidate = 0;
/*     */     
/*     */ 
/*     */     for (;;)
/*     */     {
/* 548 */       int next = (int)((candidate + 1) / generator.nextDouble());
/* 549 */       if ((next < 0) || (next >= buckets)) break;
/* 550 */       candidate = next;
/*     */     }
/* 552 */     return candidate;
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
/*     */   public static HashCode combineOrdered(Iterable<HashCode> hashCodes)
/*     */   {
/* 567 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 568 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 569 */     int bits = ((HashCode)iterator.next()).bits();
/* 570 */     byte[] resultBytes = new byte[bits / 8];
/* 571 */     for (HashCode hashCode : hashCodes) {
/* 572 */       byte[] nextBytes = hashCode.asBytes();
/* 573 */       Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");
/*     */       
/* 575 */       for (int i = 0; i < nextBytes.length; i++) {
/* 576 */         resultBytes[i] = ((byte)(resultBytes[i] * 37 ^ nextBytes[i]));
/*     */       }
/*     */     }
/* 579 */     return HashCode.fromBytesNoCopy(resultBytes);
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
/*     */   public static HashCode combineUnordered(Iterable<HashCode> hashCodes)
/*     */   {
/* 592 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 593 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 594 */     byte[] resultBytes = new byte[((HashCode)iterator.next()).bits() / 8];
/* 595 */     for (HashCode hashCode : hashCodes) {
/* 596 */       byte[] nextBytes = hashCode.asBytes();
/* 597 */       Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");
/*     */       
/* 599 */       for (int i = 0; i < nextBytes.length; tmp102_100++) {
/* 600 */         int tmp102_100 = i; byte[] tmp102_99 = resultBytes;tmp102_99[tmp102_100] = ((byte)(tmp102_99[tmp102_100] + nextBytes[tmp102_100]));
/*     */       }
/*     */     }
/* 603 */     return HashCode.fromBytesNoCopy(resultBytes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static int checkPositiveAndMakeMultipleOf32(int bits)
/*     */   {
/* 610 */     Preconditions.checkArgument(bits > 0, "Number of bits must be positive");
/* 611 */     return bits + 31 & 0xFFFFFFE0;
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
/*     */   public static HashFunction concatenating(HashFunction first, HashFunction second, HashFunction... rest)
/*     */   {
/* 627 */     List<HashFunction> list = new ArrayList();
/* 628 */     list.add(first);
/* 629 */     list.add(second);
/* 630 */     for (HashFunction hashFunc : rest) {
/* 631 */       list.add(hashFunc);
/*     */     }
/* 633 */     return new ConcatenatedHashFunction((HashFunction[])list.toArray(new HashFunction[0]), null);
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
/*     */   public static HashFunction concatenating(Iterable<HashFunction> hashFunctions)
/*     */   {
/* 647 */     Preconditions.checkNotNull(hashFunctions);
/*     */     
/* 649 */     List<HashFunction> list = new ArrayList();
/* 650 */     for (HashFunction hashFunction : hashFunctions) {
/* 651 */       list.add(hashFunction);
/*     */     }
/* 653 */     Preconditions.checkArgument(list.size() > 0, "number of hash functions (%s) must be > 0", list.size());
/* 654 */     return new ConcatenatedHashFunction((HashFunction[])list.toArray(new HashFunction[0]), null);
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
/*     */     private final int bits;
/*     */     
/*     */     private ConcatenatedHashFunction(HashFunction... functions) {
/* 661 */       super();
/* 662 */       int bitSum = 0;
/* 663 */       for (HashFunction function : functions) {
/* 664 */         bitSum += function.bits();
/* 665 */         Preconditions.checkArgument(function
/* 666 */           .bits() % 8 == 0, "the number of bits (%s) in hashFunction (%s) must be divisible by 8", function
/*     */           
/* 668 */           .bits(), function);
/*     */       }
/*     */       
/* 671 */       this.bits = bitSum;
/*     */     }
/*     */     
/*     */     HashCode makeHash(Hasher[] hashers)
/*     */     {
/* 676 */       byte[] bytes = new byte[this.bits / 8];
/* 677 */       int i = 0;
/* 678 */       for (Hasher hasher : hashers) {
/* 679 */         HashCode newHash = hasher.hash();
/* 680 */         i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
/*     */       }
/* 682 */       return HashCode.fromBytesNoCopy(bytes);
/*     */     }
/*     */     
/*     */     public int bits()
/*     */     {
/* 687 */       return this.bits;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object)
/*     */     {
/* 692 */       if ((object instanceof ConcatenatedHashFunction)) {
/* 693 */         ConcatenatedHashFunction other = (ConcatenatedHashFunction)object;
/* 694 */         return Arrays.equals(this.functions, other.functions);
/*     */       }
/* 696 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 701 */       return Arrays.hashCode(this.functions) * 31 + this.bits;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class LinearCongruentialGenerator
/*     */   {
/*     */     private long state;
/*     */     
/*     */ 
/*     */     public LinearCongruentialGenerator(long seed)
/*     */     {
/* 713 */       this.state = seed;
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 717 */       this.state = (2862933555777941757L * this.state + 1L);
/* 718 */       return ((int)(this.state >>> 33) + 1) / 2.147483648E9D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\hash\Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */