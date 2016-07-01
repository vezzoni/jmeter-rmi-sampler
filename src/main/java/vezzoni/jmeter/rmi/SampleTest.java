package vezzoni.jmeter.rmi;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.*;
import javax.rmi.PortableRemoteObject;
import org.apache.jmeter.config.Argument;
import vezzoni.rmi.spring.server.Greetings;


/**
 * Author: MENG WANG
 * Date: Sep 13, 2007
 * Time: 11:53:58 AM
 */
public class SampleTest extends AbstractJavaSamplerClient
        implements Serializable {

    private String label;
    private static final String LABEL_NAME = "Label";

    private String responseMessage;
    private static final String RESPONSE_MESSAGE_DEFAULT = "";
    private static final String RESPONSE_MESSAGE_NAME = "ResponseMessage";

    private String responseCode;
    private static final String RESPONSE_CODE_DEFAULT = "";
    private static final String RESPONSE_CODE_NAME = "ResponseCode";

    private String samplerData;
    private static final String SAMPLER_DATA_DEFAULT = "";
    private static final String SAMPLER_DATA_NAME = "SamplerData";

    private String resultData;
    private static final String RESULT_DATA_DEFAULT = "";
    private static final String RESULT_DATA_NAME = "ResultData";

    private boolean success;
    private static final String SUCCESS_DEFAULT = "OK";
    private static final String SUCCESS_NAME = "Status";

    // RMI information
    private String hostName;
    private int port;
    private String bindingName;
    private int poolSize;

    public static void main(String[] args) {
        SampleTest st = new SampleTest();
        Arguments arguments = new Arguments();
        arguments.addArgument(new Argument("ResponseMessage", "responseMessage"));
        arguments.addArgument(new Argument("ResponseCode", "responseCode"));
        arguments.addArgument(new Argument("Status", "OK"));
        arguments.addArgument(new Argument("HostName", "localhost"));
        arguments.addArgument(new Argument("Port", "1099"));
        arguments.addArgument(new Argument("BindingName", "greetings-service"));
        arguments.addArgument(new Argument("PoolSize", "1"));
        JavaSamplerContext context = new JavaSamplerContext(arguments);

        SampleResult runTest = st.runTest(context);
        System.out.println("end.");
    }
    
    public SampleTest() {
        System.out.println(whoAmI() + "\tConstruct");
    }

    private void setupValues(JavaSamplerContext
            context) {
        responseMessage = context.getParameter("ResponseMessage", "");
        responseCode = context.getParameter("ResponseCode", "");
        success = context.getParameter("Status", "OK").equalsIgnoreCase("OK");
        label = context.getParameter("Label", "");
        if (label.length() == 0) {
            label = context.getParameter("TestElement.name");
        }
        samplerData = context.getParameter("SamplerData", "");
        resultData = context.getParameter("ResultData", "");

        hostName = context.getParameter("HostName", "localhost");
        port = context.getIntParameter("Port", 1099);
        bindingName = context.getParameter("BindingName", "");
        poolSize = context.getIntParameter("PoolSize", 300);
    }

    public void setupTest(JavaSamplerContext
            context) {
        System.out.println(whoAmI() + "\tsetupTest()");
        listParameters(context);
    }

    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("Label", "");
        params.addArgument("ResponseCode", "");
        params.addArgument("ResponseMessage", "");
        params.addArgument("Status", "OK");
        params.addArgument("SamplerData", "");
        params.addArgument("ResultData", "");
        params.addArgument("HostName", "localhost");
        params.addArgument("Port", "1099");
        params.addArgument("BindingName", "RMI-SERVER");
        params.addArgument("PoolSize", "300");
        return params;
    }

    public SampleResult runTest(JavaSamplerContext
            context) {
        SampleResult results;
        setupValues(context);
        results = new SampleResult();
        results.setResponseCode(responseCode);
        results.setResponseMessage(responseMessage);
        results.setSampleLabel(label);
        if (samplerData != null && samplerData.length() > 0) {
            results.setSamplerData(samplerData);
        }
        if (resultData != null && resultData.length() > 0) {
            results.setResponseData(resultData.getBytes());
            results.setDataType("text");
        }
        try {
            RmiPool.HOST_NAME = hostName;
            RmiPool.BINDING_NAME = bindingName;
            RmiPool.PORT = port;
            RmiPool.POOL_SIZE = poolSize;

//            RmiPool rmiPool = RmiPool.getInstance();
            results.sampleStart();

            ////////////////////////////////
            // WRITE YOUR test here
            myTest();
            ////////////////////////////////
            results.setSuccessful(true);
//            results.setSuccessful(success);
            results.sampleEnd();

        } catch (Exception ex) {
            System.out.println("JavaTest: error during sample: " + ex);
            results.setSuccessful(false);
            results.sampleEnd();
        }

        System.out.println(whoAmI() + "\trunTest()" + "\tTime:\t" + results.getTime());
        listParameters(context);

        return results;
    }

    public void teardownTest(JavaSamplerContext
            context) {
        System.out.println(whoAmI() + "\tteardownTest()");
        listParameters(context);
    }

    private void listParameters(JavaSamplerContext
            context) {
        String name;
        for (Iterator argsIt = context.getParameterNamesIterator(); argsIt.hasNext(); System.out.println(name + "=" + context.getParameter(name)))
        {
            name = (String) argsIt.next();
        }
    }

    private String whoAmI() {
        StringBuilder sb = new StringBuilder();
        sb.append(Thread.currentThread().toString());
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        return sb.toString();
    }


    public void myTest() throws Exception {
        RmiPool rmiPool = RmiPool.getInstance();
        RmiConnection rmiConnection = null;
        try {
            rmiConnection = rmiPool.getConnection();
            try {
                Remote remote = rmiConnection.getRemoteInterface();
//                Greetings greetings = (Greetings) remote;
//                Greetings greetings = (Greetings) PortableRemoteObject.narrow(remote, Greetings.class);
//
//                String message = greetings.sayHello();
//                System.out.println("Message: " + message);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
//            HelloInterface hello =(HelloInterface)
//            Naming.lookup ("//192.168.10.201/Hello");
//            String narrow = (String) PortableRemoteObject.narrow(remote, String.class);
            // NOTE Write your RMI test here

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (rmiConnection != null) {
                rmiPool.releaseConnection(rmiConnection);
            }
        }

    }

}
