package uk.co.epsilontechnologies.quantify;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class ClassAdapter extends ClassNode {

    private final ClassVisitor classVisitor;
    private final Instrumentation instrumentation;

    public ClassAdapter(
            final ClassVisitor classVisitor,
            final Instrumentation instrumentation) {
        this.classVisitor = classVisitor;
        this.instrumentation = instrumentation;
    }

    @Override
    public void visitEnd() {
        for (final MethodNode methodNode : (List<MethodNode>) methods) {
            if (instrumentation.apply(this, methodNode)) {
                instrumentation.instrument(this, methodNode);
            }
        }
        accept(classVisitor);
    }

}
