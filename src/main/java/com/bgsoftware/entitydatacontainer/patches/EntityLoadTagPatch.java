package com.bgsoftware.entitydatacontainer.patches;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class EntityLoadTagPatch implements IPatch {

    @Override
    public void apply(CtClass ctClass) throws CannotCompileException {
        ClassPool classPool = ctClass.getClassPool();

        // Import all the required classes
        classPool.importPackage("net.minecraft.server.v1_8_R3.NBTTagCompound");

        for (CtMethod method : ctClass.getDeclaredMethods()) {
            if (method.getLongName().equals("net.minecraft.server.v1_8_R3.Entity.f(net.minecraft.server.v1_8_R3.NBTTagCompound)")) {
                method.insertBefore("this.entityDataContainerTag = nbttagcompound.getCompound(\"EntityDataContainer\");");
                return;
            }
        }
    }
}
