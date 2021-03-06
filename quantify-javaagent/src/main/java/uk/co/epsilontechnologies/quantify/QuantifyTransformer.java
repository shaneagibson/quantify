package uk.co.epsilontechnologies.quantify;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.UUID;

public class QuantifyTransformer implements ClassFileTransformer {

    private static final UUID uuid = UUID.randomUUID();

    public static void premain(
            final String agentArgs,
            final Instrumentation instrumentation) {
        instrumentation.addTransformer(new QuantifyTransformer());
    }

    @Override
    public byte[] transform(
            final ClassLoader loader,
            final String className,
            final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer) throws IllegalClassFormatException {
        final ClassWriter classWriter = new ClassWriter(0);
        final ClassVisitor classVisitor = new ClassAdapter(classWriter, new QuantifyInstrumentation(uuid));
        final ClassReader classReader = new ClassReader(classfileBuffer);
        classReader.accept(classVisitor, 0);
        return classWriter.toByteArray();
    }

}