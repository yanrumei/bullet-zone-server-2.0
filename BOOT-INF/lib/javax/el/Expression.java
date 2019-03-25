package javax.el;

import java.io.Serializable;

public abstract class Expression
  implements Serializable
{
  private static final long serialVersionUID = -6663767980471823812L;
  
  public abstract String getExpressionString();
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
  
  public abstract boolean isLiteralText();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\Expression.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */