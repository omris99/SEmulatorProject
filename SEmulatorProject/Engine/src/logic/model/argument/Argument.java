package logic.model.argument;

import logic.model.argument.constant.Constant;
import logic.model.argument.variable.VariableImpl;
import logic.model.program.Function;

import java.io.Serializable;

public interface Argument extends Serializable {
    String getRepresentation();
    int getIndex();

//    static Argument parse(String stringArgument) {
//        if(VariableImpl.stringVarTypeToVariableType(stringArgument) != null){
//            return VariableImpl.parse(stringArgument);
//        }
//        else if(stringArgument.startsWith("(") && stringArgument.endsWith(")")){
//            return new Function()
//        }
//    }
}
