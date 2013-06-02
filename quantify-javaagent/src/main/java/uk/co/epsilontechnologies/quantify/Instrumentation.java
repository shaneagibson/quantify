package uk.co.epsilontechnologies.quantify;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public interface Instrumentation {

    boolean apply(final ClassNode classNode, final MethodNode methodNode);

    void instrument(final ClassNode classNode, final MethodNode methodNode);

}
