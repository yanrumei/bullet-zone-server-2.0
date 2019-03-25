package javax.el;

public abstract class VariableMapper
{
  public abstract ValueExpression resolveVariable(String paramString);
  
  public abstract ValueExpression setVariable(String paramString, ValueExpression paramValueExpression);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\VariableMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */