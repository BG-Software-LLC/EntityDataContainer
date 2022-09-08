package com.bgsoftware.entitydatacontainer;

import com.bgsoftware.entitydatacontainer.patches.EntityDataContainerTagFieldPatch;
import com.bgsoftware.entitydatacontainer.patches.EntityLoadTagPatch;
import com.bgsoftware.entitydatacontainer.patches.EntitySaveTagPatch;
import com.bgsoftware.entitydatacontainer.patches.IPatch;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class EntityDataContainerTransformer implements ClassFileTransformer {

    private static final Logger logger = Logger.getLogger("EntityDataContainer");

    private static final Map<String, Collection<IPatch>> patches = createPatches();

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String fixedClassName = className.replace("/", ".");

        Collection<IPatch> patchesToApply = patches.get(fixedClassName);

        if (patchesToApply != null) {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass;

            try {
                ctClass = classPool.get(fixedClassName);
            } catch (NotFoundException ex) {
                logger.warning("Failed to transform class " + fixedClassName + ":");
                ex.printStackTrace();
                return null;
            }

            for (IPatch patch : patchesToApply) {
                try {
                    patch.apply(ctClass);
                } catch (CannotCompileException ex) {
                    logger.warning("Failed to apply patch " + patch + ":");
                    ex.printStackTrace();
                }
            }

            try {
                return ctClass.toBytecode();
            } catch (IOException | CannotCompileException ex) {
                logger.warning("Failed to build class " + fixedClassName + ":");
                ex.printStackTrace();
            }
        }

        return null;
    }

    private static Map<String, Collection<IPatch>> createPatches() {
        Map<String, Collection<IPatch>> patches = new HashMap<>();

        patches.put("net.minecraft.server.v1_8_R3.Entity", Arrays.asList(new EntityDataContainerTagFieldPatch(),
                new EntitySaveTagPatch(), new EntityLoadTagPatch()));

        return Collections.unmodifiableMap(patches);
    }

}
