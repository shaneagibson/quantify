package uk.co.epsilontechnologies.quantify.persistence;

import uk.co.epsilontechnologies.quantify.model.MethodInvocation;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationDataStore {

    private final List<MethodInvocation> invocations = new ArrayList<>();

    private static MethodInvocationDataStore instance;

    public static MethodInvocationDataStore getInstance() {
        if (instance == null) {
            instance = new MethodInvocationDataStore();
        }
        return instance;
    }

    private MethodInvocationDataStore() {
        super();
    }

    public void clear() {
        this.invocations.clear();
    }

    public List<MethodInvocation> getAll() {
        return this.invocations;
    }

    public void add(final MethodInvocation methodInvocation) {
        this.invocations.add(methodInvocation);
    }
}