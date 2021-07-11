package com.lhever.common.core.support.leak;

public  class StackTraceAccessor extends Throwable {

    public StackTraceAccessor() {
        super();
    }

    public void A() {
        System.out.println("method A");
        B();
    }

    public void B() {
        System.out.println("method B");
        C();
    }

    public void C() {
        System.out.println("method C");
        D();
    }

    public void D() {
        System.out.println("method D");
        StackTraceElement[] array = getStackTrace();
        for (StackTraceElement element : array) {
            //System.out.println(element.getClassName() + ":  " + element.getMethodName() + ": " + element.getLineNumber());
            System.out.println(element.toString());
        }
    }


    public void E() {
        System.out.println("method E");
        F();
    }

    public void F() {
        System.out.println("method F");
        G();
    }

    public void G() {
        System.out.println("method G");
        StackTraceAccessor newAccessor = new StackTraceAccessor();
        StackTraceElement[] array = newAccessor.getStackTrace();
        //这里打印的StackTraceElement是在方法G中new出来的StackTraceAccessor类型的新对象newAccessor的栈帧
        for (StackTraceElement element : array) {
            System.out.println(element.toString());
        }
    }





}
