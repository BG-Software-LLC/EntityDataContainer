package com.bgsoftware.entitydatacontainer.patches;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class EntitySaveTagPatch implements IPatch {

    @Override
    public void apply(CtClass ctClass) throws CannotCompileException {
        ClassPool classPool = ctClass.getClassPool();

        // Import all the required classes
        classPool.importPackage("net.minecraft.server.v1_8_R3.NBTTagCompound");

        for (CtMethod method : ctClass.getDeclaredMethods()) {
            if (method.getLongName().equals("net.minecraft.server.v1_8_R3.Entity.e(net.minecraft.server.v1_8_R3.NBTTagCompound)")) {
                // This is the serialize nbt tag method
                method.insertBefore("nbttagcompound.set(\"EntityDataContainer\", this.entityDataContainerTag);");
                return;
            }
        }
    }
}
