package com.lhever.common.core.support.leak;


public class ResourceLeakDetectorTest {

    private static  TrackableObject obj = new TrackableObject();

    public static void testStackTraceA() {
        StackTraceAccessor accessor = new StackTraceAccessor();
        accessor.A();
    }

    public static void testStackTraceB() {
        testStackTraceB_();
    }

    public static void testStackTraceB_() {
        //对象StackTraceAccessor在哪个方法内部创建，方法调用栈就记录到哪个方法
        StackTraceAccessor accessor = new StackTraceAccessor();
        accessor.A();
    }


    public static void testStackTraceC() {
        StackTraceAccessor accessor = new StackTraceAccessor();
        accessor.E();
    }

    /**
     * 测试StackTraceElement的作用
     * @author lihong10 2019/2/27 16:50
     * @param
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/2/27 16:50
     * @modify by reason:{原因}
     */
    public static  void main(String[] args) {

        testStackTraceA();

        testStackTraceB();

        testStackTraceC();


        testResourceLeakDetector();
        testResourceLeakDetector2();
    }



    public static class TrackableObject implements ResourceLeakHint {
        @Override
        public String toHintString() {
            return "I am a obj can be tracked";
        }
    }





    public static  void testResourceLeakDetector() {
        System.out.println();
        System.out.println("testResourceLeakDetector() called !!!");

        ResourceLeakDetectorFactory factory = ResourceLeakDetectorFactory.instance();
        ResourceLeakDetector<TrackableObject> detector = factory.newResourceLeakDetector(TrackableObject.class, 0);
        detector.setLevel(ResourceLeakDetector.Level.PARANOID);

        ResourceLeakTracker track = track(detector);
        track.record(obj);
        track.record(obj);
        track.record(obj);
        track.record(obj);
        track.record(obj);




        System.out.println(track);
        track.close(obj);

        System.out.println();
        System.out.println("************************************************");
        System.out.println(track);
        System.out.println();



    }

    private static ResourceLeakTracker track(ResourceLeakDetector<TrackableObject> detector) {

        ResourceLeakTracker<TrackableObject> track = detector.track(obj);
        return track;
    }



    public static  void testResourceLeakDetector2() {
        System.out.println();
        System.out.println("testResourceLeakDetector2() called !!!");

        ResourceLeakDetectorFactory factory = ResourceLeakDetectorFactory.instance();
        ResourceLeakDetector<TrackableObject> detector = factory.newResourceLeakDetector(TrackableObject.class, 0);
        detector.setLevel(ResourceLeakDetector.Level.PARANOID);

        ResourceLeakTracker track = track2(detector);
        track.record();
        track.record();
        track.record();
        track.record();
        track.record();

        System.out.println(track);

        System.out.println();
        System.out.println("************************************************");
        System.out.println(track);
        System.out.println();
    }

    private static ResourceLeakTracker track2(ResourceLeakDetector<TrackableObject> detector) {

        TrackableObject obj2 = new TrackableObject();
        ResourceLeakTracker<TrackableObject> track = detector.track(obj2);
        obj2 = null;
        return track;
    }


}
