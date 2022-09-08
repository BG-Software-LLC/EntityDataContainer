package com.bgsoftware.entitydatacontainer.patches;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

public class EntityDataContainerTagFieldPatch implements IPatch {

    @Override
    public void apply(CtClass ctClass) throws CannotCompileException {
        ClassPool classPool = ctClass.getClassPool();

        // Import all the required classes
        classPool.importPackage("net.minecraft.server.v1_8_R3.NBTTagCompound");

        CtField ctField = CtField.make("public net.minecraft.server.v1_8_R3.NBTTagCompound entityDataContainerTag = new net.minecraft.server.v1_8_R3.NBTTagCompound();", ctClass);
        ctClass.addField(ctField);
    }

}
