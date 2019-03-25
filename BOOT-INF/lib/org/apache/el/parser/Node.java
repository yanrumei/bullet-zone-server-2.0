package org.apache.el.parser;

import javax.el.ELException;
import javax.el.MethodInfo;
import javax.el.ValueReference;
import org.apache.el.lang.EvaluationContext;

public abstract interface Node
{
  public abstract void jjtOpen();
  
  public abstract void jjtClose();
  
  public abstract void jjtSetParent(Node paramNode);
  
  public abstract Node jjtGetParent();
  
  public abstract void jjtAddChild(Node paramNode, int paramInt);
  
  public abstract Node jjtGetChild(int paramInt);
  
  public abstract int jjtGetNumChildren();
  
  public abstract String getImage();
  
  public abstract Object getValue(EvaluationContext paramEvaluationContext)
    throws ELException;
  
  public abstract void setValue(EvaluationContext paramEvaluationContext, Object paramObject)
    throws ELException;
  
  public abstract Class<?> getType(EvaluationContext paramEvaluationContext)
    throws ELException;
  
  public abstract boolean isReadOnly(EvaluationContext paramEvaluationContext)
    throws ELException;
  
  public abstract void accept(NodeVisitor paramNodeVisitor)
    throws Exception;
  
  public abstract MethodInfo getMethodInfo(EvaluationContext paramEvaluationContext, Class<?>[] paramArrayOfClass)
    throws ELException;
  
  public abstract Object invoke(EvaluationContext paramEvaluationContext, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject)
    throws ELException;
  
  public abstract ValueReference getValueReference(EvaluationContext paramEvaluationContext);
  
  public abstract boolean isParametersProvided();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\Node.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */