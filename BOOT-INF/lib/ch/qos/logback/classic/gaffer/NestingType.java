package ch.qos.logback.classic.gaffer;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.util.LinkedHashMap;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.codehaus.groovy.transform.ImmutableASTTransformation;

public enum NestingType
  implements GroovyObject
{
  public static final NestingType MIN_VALUE;
  public static final NestingType MAX_VALUE;
  
  public NestingType(LinkedHashMap __namedArgs)
  {
    MetaClass localMetaClass = $getStaticMetaClass();
    this.metaClass = localMetaClass;
    if ((!BytecodeInterface8.isOrigZ()) || (BytecodeInterface8.disabledStandardMetaClass()))
    {
      if (ScriptBytecodeAdapter.compareEqual(__namedArgs, null)) {
        throw ((Throwable)arrayOfCallSite[0].callConstructor(IllegalArgumentException.class, "One of the enum constants for enum ch.qos.logback.classic.gaffer.NestingType was initialized with null. Please use a non-null value or define your own constructor."));
      } else {
        arrayOfCallSite[1].callStatic(ImmutableASTTransformation.class, this, __namedArgs);
      }
    }
    else if (ScriptBytecodeAdapter.compareEqual(__namedArgs, null)) {
      throw ((Throwable)arrayOfCallSite[2].callConstructor(IllegalArgumentException.class, "One of the enum constants for enum ch.qos.logback.classic.gaffer.NestingType was initialized with null. Please use a non-null value or define your own constructor."));
    } else {
      arrayOfCallSite[3].callStatic(ImmutableASTTransformation.class, this, __namedArgs);
    }
  }
  
  public NestingType()
  {
    this((LinkedHashMap)ScriptBytecodeAdapter.castToType(arrayOfCallSite[4].callConstructor(LinkedHashMap.class), LinkedHashMap.class));
  }
  
  static
  {
    __$swapInit();
    Object localObject1 = $getCallSiteArray()[18].callStatic(NestingType.class, "NA", Integer.valueOf(0));
    NA = (NestingType)ShortTypeHandling.castToEnum(localObject1, NestingType.class);
    Object localObject2 = $getCallSiteArray()[19].callStatic(NestingType.class, "SINGLE", Integer.valueOf(1));
    SINGLE = (NestingType)ShortTypeHandling.castToEnum(localObject2, NestingType.class);
    Object localObject3 = $getCallSiteArray()[20].callStatic(NestingType.class, "SINGLE_WITH_VALUE_OF_CONVENTION", Integer.valueOf(2));
    SINGLE_WITH_VALUE_OF_CONVENTION = (NestingType)ShortTypeHandling.castToEnum(localObject3, NestingType.class);
    Object localObject4 = $getCallSiteArray()[21].callStatic(NestingType.class, "AS_COLLECTION", Integer.valueOf(3));
    AS_COLLECTION = (NestingType)ShortTypeHandling.castToEnum(localObject4, NestingType.class);
    NestingType localNestingType1 = NA;
    MIN_VALUE = localNestingType1;
    NestingType localNestingType2 = AS_COLLECTION;
    MAX_VALUE = localNestingType2;
    NestingType[] arrayOfNestingType = { NA, SINGLE, SINGLE_WITH_VALUE_OF_CONVENTION, AS_COLLECTION };
    $VALUES = arrayOfNestingType;
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\gaffer\NestingType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */