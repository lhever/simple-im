package com.lhever.common.core.support.parser.markdown;

import org.junit.Test;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/4/9 11:59
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/9 11:59
 * @modify by reason:{方法名}:{原因}
 */
public class URLParser extends MDURLParser {

    private static String start = "[";
    private static String middle = "](";
    private static String end = ")";


    public URLParser() {
        super(start, middle, end);
    }


    @Override
    public String handle(String text, int offset, int startIndex, int middleIndex, int endIndex, String title, String url) {
        System.out.println("title: " + title + ",  url: " + url);
        return url;
    }


    @Test
    public void testParse1() {

        String content = "存储资源能力介绍jfsknkdsvjdk4.png)";

        //预热
        String result = parseUrl(content, 0);
        System.out.println(result);

        System.out.println(result.equals(content));
    }

    @Test
    public void testParse2() {

        String content = "存储资源能力![介绍jfsknkdsvjdk4.png)";

        //预热
        String result = parseUrl(content, 0);
        System.out.println(result);

        System.out.println(result.equals(content));
    }


    @Test
    public void testParse3() {

        String content = "存储资源能力![介绍](jfsknkdsvjdk4.png";

        //预热
        String result = parseUrl(content, 0);
        System.out.println(result);

        System.out.println(result.equals(content));
    }

    @Test
    public void testParse3_1() {

        String content = "存储资源能力![介绍](jfsknkdsvjdk4.png)xxxx![";

        //预热
        String result = parseUrl(content, 0);
        System.out.println(result);

        System.out.println(result.equals(content));
    }

    @Test
    public void testParse3_2() {

        String content = "存储资源能力![介绍](jfsknkdsvjdk4.png)xxxx![xxxx](xxxx";

        //预热
        String result = parseUrl(content, 0);
        System.out.println(result);

        System.out.println(result.equals(content));
    }


    @Test
    public void testParse4() {

        String content = "存储资源能力介绍![](/imgs/存储能力开放介绍.png)我是中托管![](/imgs/5d2f4d8a8e0a8dea9bfa04910fb3b071.png)" +
                "![img](/imgs/image3.png)提供视频、![]xx  ![](/imgs/xxx.png)图片、文件、对象数据统一存储能力![](/imgs/image4.png)";

        //预热
        String result = parseUrl(content, 0);
        System.out.println(result);

        System.out.println(result.equals(content));
    }


    @Test
    public void testParse5() {

        String content = "\n" +
                "# 门禁应用服务开放能力\n" +
                "\n" +
                "![img](/imgs/能力概览-门禁应用服务.png)\n" +
                "\n" +
                "-   提供远程开门、关门、设置门常开、设置门常闭的功能，支持一次性批量操作10个门禁点。\n" +
                "\n" +
                "-   提供了门禁事件查询和事件关联图片获取能力，可以根据检索字段分页查询门禁事件，并根据事件中返回的图片地址信息调用获取事件图片的接口，来获取事件关联的图片。\n" +
                "\n" +
                "-   提供了门禁事件实时订阅功能，订阅相应的门禁事件类型之后，事件产生之后，对接方会接收到对应的事件报文信息。\n" +
                "\n" +
                "-   提供了门禁权限的下发和查询的功能，可以根据门禁的设备信息，人员卡片，人脸等信息，将权限下发至对应的设备。门禁权限下发后，可以查询到门禁权限的下发记录。\n" +
                "\n" +
                "-   提供了此能力的平台：[综合安防管理平台](/docs/37e38899e583cfe4f9879a07a5294bf4#a481a121)。\n" +
                "\n" +
                "# 门禁应用开发\n" +
                "\n" +
                "## 获取门禁事件及图片开发\n" +
                "\n" +
                "**应用场景举例**\n" +
                "\n" +
                "开发者可以通过查询门禁事件，来获取门禁的刷卡记录明细记录。门禁事件信息中可以包括门禁点信息，人员信息，门禁事件类型等信息，且如果门禁设备带有抓图功能并开启一体机联动抓图时，门禁事件中会含有图片信息。可以在获取门禁事件的同时，获取到事件关联图片信息，再通过获取图片的接口获取到相应的门禁事件图片。\n" +
                "\n" +
                "**流程描述**\n" +
                "\n" +
                "1.  调用【[查询门禁点事件](/docs/6df2abb4f76104afa1ffedc7cad47d93#d40dcdf7)】的接口，获取门禁事件的返回信息。\n" +
                "\n" +
                "2.  根据返回事件数据中的picUri（图片相对地址）和svrIndexCode（存储接入服务唯一标识），再根据这两个参数请求【[获取门禁事件的图片](/docs/6df2abb4f76104afa1ffedc7cad47d93#ceea3765)】接口，可以获取到事件的图片。\n" +
                "\n" +
                "![img](/imgs/编程引导-获取门禁事件及图片.png)\n" +
                "\n" +
                "## 门禁点反控开发\n" +
                "\n" +
                "**应用场景举例**\n" +
                "\n" +
                "门禁点反控应用于需要对门禁点进行反控操作的场景，如发生紧急事件，保安人员需要立即开门或关门，该操作通常被集到PC客户端、手机APP等应用端，便于保安人员使用。\n" +
                "\n" +
                "**流程描述**\n" +
                "\n" +
                "1.  首先通过资源目录服务【[获取门禁点信息列表](/docs/93d926415b62e0f76290e6a63cd6facb#ffba6708)】获取门禁点信息。\n" +
                "\n" +
                "2.  通过【[门禁点反控](/docs/6df2abb4f76104afa1ffedc7cad47d93#aa6d6de0)】接口实现对门状态的控制。完成开门、关门、设置常开、设置常闭操作。\n" +
                "\n" +
                "![img](/imgs/编程引导-门禁点反控开发.png)\n" +
                "\n" +
                "## 门禁权限下发开发\n" +
                "\n" +
                "**应用场景举例**\n" +
                "\n" +
                "用于综合大楼、学校、医院等应用场景，可以支持将人员的卡片、人脸、指纹等介质信息下发到门禁设备。从而人员可具备对应的设备相应的权限，如门禁开门权限。如果人员的权限出现变更，可以灵活地在对接的应用平台上进行权限调整。\n" +
                "\n" +
                "**流程描述**\n" +
                "\n" +
                "1.  根据业务选择需要下发的权限类型，调用【[创建下载任务]()】接口。\n" +
                "\n" +
                "2.  调用【[下载任务中添加数据]()】接口，添加相应的人员，门禁设备等信息。\n" +
                "\n" +
                "3.  获取到【[创建下载任务]()】接口返回的taskId，调用【[开始下载任务]()】接口，进行权限下发，此处的权限下发为异步下发。\n" +
                "\n" +
                "4.  下载过程当中可以调用【[查询下载任务进度]()】接口进行下载任务进度查询，如果下载任务完成，则说明门禁权限下发成功。\n" +
                "\n" +
                "![img](/imgs/编程引导-门禁权限下发开发.png)\n" +
                "\n" +
                "<br/>\n" +
                "<br/>\n" +
                "<br/>\n" +
                "<br/>\n" +
                "<br/>";

        //预热
        String result = parseUrl(content, 0);
        System.out.println(result);

        System.out.println(result.equals(content));
    }
}
