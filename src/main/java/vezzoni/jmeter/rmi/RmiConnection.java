package vezzoni.jmeter.rmi;

import java.rmi.Remote;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;

public class RmiConnection {

    private Remote remoteInterface;
    private boolean inUse;
    private Map<String, Method> methodsLookup;

    /**
     * Constructor
     * @param rmiConnection  RMI connection
     */
    public RmiConnection(Remote rmiConnection) {
        this.remoteInterface = rmiConnection;
    }

    /**
     * Constructor
     */
    public RmiConnection() {
        inUse = false;
        remoteInterface = null;
    }

    public Remote getRemoteInterface() {
        return remoteInterface;
    }

    public void setRemoteInterface(Remote remoteInterface) {
        this.remoteInterface = remoteInterface;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    /**
     * Retrieve all methods
     */
    public void retrieveMethods() {
        methodsLookup = new HashMap<String, Method>(10);
        Method[] methods = remoteInterface.getClass().getMethods();
        for (Method method: methods) {
            methodsLookup.put(method.getName(), method);
        }
    }

    /**
     * Find a particular method by name
     * @param methodName Method name
     * @return  Method
     */
    public Method findMethod(String methodName) {
        return methodsLookup.get(methodName);
    }


}
