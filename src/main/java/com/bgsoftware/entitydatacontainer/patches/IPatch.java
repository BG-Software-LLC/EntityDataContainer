package com.bgsoftware.entitydatacontainer.patches;


import javassist.CannotCompileException;
import javassist.CtClass;

public interface IPatch {

    void apply(CtClass ctClass) throws CannotCompileException;

}
