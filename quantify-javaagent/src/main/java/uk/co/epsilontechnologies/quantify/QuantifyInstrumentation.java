package uk.co.epsilontechnologies.quantify;

import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class QuantifyInstrumentation implements Opcodes, Instrumentation {

    private static final List<String> IGNORED_CLASSES = Arrays.asList(
            "com/intellij",
            "javax/management/remote",
            "com/sun",
            "org/omg",
            "sun/rmi",
            "sun/reflect",
            "org/slf4j",
            "org/apache/log4j",
            "uk/co/epsilontechnologies/quantify"
    );

    @Override
    public boolean apply(final ClassNode classNode, final MethodNode methodNode) {
        boolean validAccess = !((methodNode.access & ACC_NATIVE) != 0 ||
                (methodNode.access & ACC_BRIDGE) != 0 ||
                (methodNode.access & ACC_SYNTHETIC) != 0 ||
                (methodNode.access & ACC_VOLATILE) != 0 ||
                (methodNode.access & ACC_INTERFACE) != 0);
        if (!validAccess) {
            return false;
        }
        for (final String ignoredClass : IGNORED_CLASSES) {
            if (classNode.name.startsWith(ignoredClass)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void instrument(
            final ClassNode classNode,
            final MethodNode methodNode) {

        final String className = classNode.name;
        final String methodName = resolveMethodSignature(methodNode);

        final InsnList beginList = createList(
                        new MethodInsnNode(INVOKESTATIC, "java/lang/System", "nanoTime", "()J"),
                        new VarInsnNode(LSTORE, methodNode.maxLocals + 1));

        final Iterator<AbstractInsnNode> insnNodes = (Iterator<AbstractInsnNode>) methodNode.instructions.iterator();

        methodNode.instructions.insert(beginList);

        while (insnNodes.hasNext()){

            final AbstractInsnNode insn = insnNodes.next();

            if (insn.getOpcode() == Opcodes.IRETURN ||
                insn.getOpcode() == Opcodes.RETURN ||
                insn.getOpcode() == Opcodes.ARETURN ||
                insn.getOpcode() == Opcodes.LRETURN ||
                insn.getOpcode() == Opcodes.DRETURN) {

                final InsnList endList =
                        createList(
                                new MethodInsnNode(INVOKESTATIC, "java/lang/System", "nanoTime", "()J"),
                                new VarInsnNode(LSTORE, methodNode.maxLocals + 3),
                                new MethodInsnNode(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;"),
                                new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Thread", "getName", "()Ljava/lang/String;"),
                                new VarInsnNode(ASTORE, 0),
                                new FieldInsnNode(GETSTATIC, "uk/co/epsilontechnologies/quantify/JmxNotifier", "instance", "Luk/co/epsilontechnologies/quantify/JmxNotifier;"),
                                new LdcInsnNode(className),
                                new LdcInsnNode(methodName),
                                new VarInsnNode(ALOAD, 0),
                                new VarInsnNode(LLOAD, methodNode.maxLocals + 1),
                                new VarInsnNode(LLOAD, methodNode.maxLocals + 3),
                                new MethodInsnNode(INVOKEVIRTUAL, "uk/co/epsilontechnologies/quantify/JmxNotifier", "notify", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJ)V"));

                methodNode.instructions.insertBefore(insn, endList);
            }

        }

        methodNode.maxLocals += 20;
        methodNode.maxStack += 20;
    }

    private String resolveMethodSignature(final MethodNode methodNode) {
        final Type[] argumentTypes = Type.getArgumentTypes(methodNode.desc);
        final StringBuilder stringBuilder = new StringBuilder(methodNode.name);
        stringBuilder.append("(");
        if (argumentTypes.length > 0) {
            for (final Type argumentType : argumentTypes) {
                stringBuilder.append(argumentType.toString());
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private InsnList createList(final AbstractInsnNode... nodes) {
        final InsnList list = new InsnList();
        for (final AbstractInsnNode node : nodes) {
            list.add(node);
        }
        return list;
    }

}