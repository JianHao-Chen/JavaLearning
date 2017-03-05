package jdkSrc.Proxy;

public class BusinessProcessorImpl implements BusinessProcessor,Runnable {

    public void processBusiness() {
        System.out.println("processing business.....");
    }

    public int Calucate(int a, int b) {
        return a+b;
    }

    public void run() {
        System.out.println("run()");
    }
    
    
}
