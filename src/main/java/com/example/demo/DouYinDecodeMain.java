package com.example.demo;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * tips：根据群里的同学：Change QQ：11848313 分享出来的链接：https://github.com/zbfzn/douyin-clear-php得到思路
 * 大家可以去看一下php的源码（虽然我是看不懂）
 *
 * @Author haidong
 * @Date 2019年7月10日 02:18:47
 */

@Slf4j
@SuppressWarnings("all")
public class DouYinDecodeMain {

    //复制下面链接测试吧，帅气的小姐姐，重要的事情说三遍
    //#在抖音，记录美好生活##开动就现在 #轮滑 好厉害的小姐姐 http://v.douyin.com/kPJm4r/ 复制此链接，打开【抖音短视频】，直接观看视频！
    //#在抖音，记录美好生活##开动就现在 #轮滑 好厉害的小姐姐 http://v.douyin.com/kPJm4r/ 复制此链接，打开【抖音短视频】，直接观看视频！
    //#在抖音，记录美好生活##开动就现在 #轮滑 好厉害的小姐姐 http://v.douyin.com/kPJm4r/ 复制此链接，打开【抖音短视频】，直接观看视频！
    static final String API[] = {
            "https://aweme.snssdk.com/aweme/v1/aweme/detail/?origin_type=link&retry_type=no_retry&iid=74655440239&device_id=57318346369&ac=wifi&channel=wandoujia&aid=1128&app_name=aweme&version_code=140&version_name=1.4.0&device_platform=android&ssmix=a&device_type=MI+8&device_brand=xiaomi&os_api=22&os_version=5.1.1&uuid=865166029463703&openudid=ec6d541a2f7350cd&manifest_version_code=140&resolution=1080*1920&dpi=1080&update_version_code=1400&as=a13520b0e9c40d9cbd&cp=064fdf579fdd07cae1&aweme_id=",
            "https://aweme.snssdk.com/aweme/v1/aweme/detail/?origin_type=link&retry_type=no_retry&iid=74655440239&device_id=57318346369&ac=wifi&channel=wandoujia&aid=1128&app_name=aweme&version_code=140&version_name=1.4.0&device_platform=android&ssmix=a&device_type=MI+8&device_brand=xiaomi&os_api=22&os_version=5.1.1&uuid=865166029463703&openudid=ec6d541a2f7350cd&manifest_version_code=140&resolution=1080*1920&dpi=1080&update_version_code=1400&as=a13510902a54ed1cad&cp=0a40dc5ba5db09cee1&aweme_id=",
            "https://aweme.snssdk.com/aweme/v1/aweme/detail/?origin_type=link&retry_type=no_retry&iid=43619087057&device_id=57318346369&ac=wifi&channel=update&aid=1128&app_name=aweme&version_code=251&version_name=2.5.1&device_platform=android&ssmix=a&device_type=MI+8&device_brand=xiaomi&language=zh&os_api=22&os_version=5.1.1&uuid=865166029463703&openudid=ec6d541a2f7350cd&manifest_version_code=251&resolution=1080*1920&dpi=480&update_version_code=2512&as=a1e500706c54fd8c8d&cp=004ad55fc8d60ac4e1&aweme_id=",
            "https://aweme.snssdk.com/aweme/v1/aweme/detail/?origin_type=link&retry_type=no_retry&iid=43619087057&device_id=57318346369&ac=wifi&channel=update&aid=1128&app_name=aweme&version_code=251&version_name=2.5.1&device_platform=android&ssmix=a&device_type=MI+8&device_brand=xiaomi&language=zh&os_api=22&os_version=5.1.1&uuid=865166029463703&openudid=ec6d541a2f7350cd&manifest_version_code=251&resolution=1080*1920&dpi=480&update_version_code=2512&as=a10500409d74bdec1d&cp=0a4ed456dedf0acee1&aweme_id=",
            "https://aweme.snssdk.com/aweme/v1/aweme/detail/?origin_type=link&retry_type=no_retry&iid=75364831157&device_id=68299559251&ac=wifi&channel=wandoujia&aid=1128&app_name=aweme&version_code=650&version_name=6.5.0&device_platform=android&ssmix=a&device_type=xiaomi+8&device_brand=xiaomi&language=zh&os_api=22&os_version=5.1.1&openudid=2e5c5ff4ce710faf&manifest_version_code=660&resolution=1080*1920&dpi=480&update_version_code=6602&mcc_mnc=46000&js_sdk_version=1.16.2.7&as=a1257080aec45ddcad&cp=0b4cd25fe4d00ccfe1&aweme_id=",
            "https://aweme.snssdk.com/aweme/v1/aweme/detail/?origin_type=link&retry_type=no_retry&iid=75364831157&device_id=68299559251&ac=wifi&channel=wandoujia&aid=1128&app_name=aweme&version_code=650&version_name=6.5.0&device_platform=android&ssmix=a&device_type=xiaomi+8&device_brand=xiaomi&language=zh&os_api=22&os_version=5.1.1&openudid=2e5c5ff4ce710faf&manifest_version_code=660&resolution=1080*1920&dpi=480&update_version_code=6602&mcc_mnc=46000&js_sdk_version=1.16.2.7&as=a125a0b01f946d2cdd&cp=0744d553ffd60cc3e1&aweme_id=",
            "https://aweme.snssdk.com/aweme/v1/aweme/detail/?retry_type=no_retry&iid=74655440239&device_id=57318346369&ac=wifi&channel=wandoujia&aid=1128&app_name=aweme&version_code=140&version_name=1.4.0&device_platform=android&ssmix=a&device_type=MI+8&device_brand=xiaomi&os_api=22&os_version=5.1.1&uuid=865166029463703&openudid=ec6d541a2f7350cd&manifest_version_code=140&resolution=1080*1920&dpi=1080&update_version_code=1400&as=a125372f1c487cb50f&cp=728dcc5bc7f4f558e1&aweme_id="
    };

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        while (true) {
            try {
                System.out.println("请输入您要解析的视频连接(可以直接复制到窗框):");
                String inputText = inputText();
                // 输入判断需要解析的抖音地址
                String url2 = decodeHttpUrl(inputText);
                // 去掉中文并且返回抖音http地址
                String url = decodeHttpUrl(url2);
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).cookie("cookie", "tt_webid=6711334817457341965; _ga=GA1.2.611157811.1562604418; _gid=GA1.2.1578330356.1562604418; _ba=BA0.2-20190709-51")
                            //模拟手机浏览器
                            .header("user-agent", "Mozilla/5.0 (Linux; U; Android 5.0; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1")
                            //.header("cookie","tt_webid=6711334817457341965; _ga=GA1.2.611157811.1562604418; _gid=GA1.2.1578330356.1562604418; _ba=BA0.2-20190709-51")
                            .timeout(12138).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 解析网页标签
                Elements elem = doc.getElementsByTag("script");
                String url1 = elem.toString();
                int startLen = url1.indexOf("itemId: \"");
                int endLen = url1.indexOf("\",\n" +
                        "            test_group");
                String itemId = url1.substring(startLen, endLen).replaceAll("itemId: \"", "");
                /**
                 * API里面有7个网站，可以自己选择，
                 * 最后一个是出问题的
                 * 最后一个是出问题的
                 * 最后一个是出问题的
                 */
                String result2 = HttpRequest.get(API[4] + itemId)
                        //模拟手机浏览器
                        .header(Header.USER_AGENT, "Mozilla/5.0 (Linux; U; Android 5.0; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1")//头信息，多个头信息多次调用此方法即可
                        .timeout(12138)//超时，毫秒
                        .execute().body();
                try {
                    //GOSN解析
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(result2).getAsJsonObject();
                    JsonArray asJsonArray = jsonObject.get("aweme_detail").getAsJsonObject().get("video").getAsJsonObject().get("play_addr").getAsJsonObject().get("url_list").getAsJsonArray();
                    JsonElement decode = asJsonArray.get(2);
                    if (!StringUtils.isEmpty(decode)) {
                        System.out.println("解析地址为:" + decode.toString().replaceAll("\"", ""));
                    }
                } catch (Exception e) {
                    System.out.println("解析失败，请更换地址重试,报错信息：" + e.getMessage());
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public static String decodeHttpUrl(String url) {
        // 检测是否有中文，如果没有中文就是直接地址
        boolean containChinese = isContainChinese(url);
        if (containChinese) {
            int start = url.indexOf("http");
            int end = url.lastIndexOf("/");
            String decodeurl = url.substring(start, end);
            return decodeurl;
        } else
            return url;
    }

    public static String inputText() {
        Scanner text = new Scanner(System.in);
        String inputurl = text.nextLine();
        if (StringUtils.isEmpty(inputurl)) {//这里只判断了输入为空，根据业务自己更改
            throw new RuntimeException("输入有误，请重新输入");
        } else {
            return inputurl;
        }
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
