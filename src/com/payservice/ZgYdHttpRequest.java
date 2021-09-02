package com.payservice;

import com.alibaba.fastjson.JSONObject;
import com.paybean.ZgYdBean;
import com.payinterface.ZgYdInterface;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZgYdHttpRequest {
    private static ZgYdBean bean = new ZgYdBean();
    /**
     * 10086【登录】->【获取cookie】-> 【提交json充值】代码片段
     */
    @ZgYdInterface(phone_number = "13710483662", amount = "50", chargeMoney = "50", choseMoney = "50")
    private String Phone_Number;

    public static String do10086Post() {
        /**
         * {"channel":"00","amount":100,"chargeMoney":100,"choseMoney":100,"operateId":5467,"homeProv":"200","source":"","numFlag":"0"}
         */
        List<String> list = new ArrayList<>();
        Field[] field = ZgYdHttpRequest.class.getDeclaredFields();
        for (Field field1 : field) {
            boolean is = field1.isAnnotationPresent(ZgYdInterface.class);
            if (is) {
                ZgYdInterface zgYdInterface = field1.getAnnotation(ZgYdInterface.class);
                list.add(zgYdInterface.phone_number());
                list.add(zgYdInterface.amount());
                list.add(zgYdInterface.chargeMoney());
                list.add(zgYdInterface.choseMoney());
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channel", "00");
        jsonObject.put("amount", list.get(1));
        jsonObject.put("chargeMoney", list.get(2));
        jsonObject.put("choseMoney", list.get(3));
        jsonObject.put("operateId", 5467);
        jsonObject.put("homeProv", "200");
        jsonObject.put("source", "");
        jsonObject.put("numFlag", "0");
        String result = null;
        try {
            URL url = new URL("https://shop.10086.cn/i/v1/pay/saveorder/" + list.get(0) + "?provinceId=200");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Host", "shop.10086.cn");
            connection.setRequestProperty("Origin", "https://shop.10086.cn");
            connection.setRequestProperty("payPhoneNo", list.get(0));
            connection.setRequestProperty("Referer", "https://shop.10086.cn/i/?f=rechargecredit&mobileNo="+list.get(0)+"&amount=100");
            connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
            connection.setRequestProperty("sec-ch-ua-mobile", "?0");
            connection.setRequestProperty("Sec-Fetch-Dest", "empty");
            connection.setRequestProperty("Sec-Fetch-Mode", "cors");
            connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            connection.setRequestProperty("Cookie", bean.getCookies());
            byte[] out_bytes = (jsonObject.toString()).getBytes();
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(out_bytes);
            outputStream.flush();
            outputStream.close();
            int responsecode = connection.getResponseCode();
            if (responsecode == 200) {
                InputStream inputStream = connection.getInputStream();
                byte[] in_byte = new byte[inputStream.available()];
                inputStream.read(in_byte);
                result = new String(in_byte, "UTF-8");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                inputStreamReader.getEncoding();
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("异常" + e);
        }

        return result;
    }

    public Map<String, Object> do10086toPayUrl() {
        String json = do10086Post();
        Map<String, Object> map = new HashMap<>();
        try {
            if (!json.isEmpty()) {
                map.put("payUrl", JSONObject.parseObject(json).get("data").toString());
                String tmp = map.get("payUrl").toString();
                map.put("orderId", JSONObject.parseObject(tmp).get("orderId"));
                map.put("serialNo", JSONObject.parseObject(tmp).get("serialNo").toString());
            } else {
                System.out.println("数据为空");
            }
        } catch (Exception e) {
            System.out.println("异常");
        }
        return map;
    }

    public void do10086Buy() throws IOException {
        Map<String,Object> map  = do10086toPayUrl();
        String orderID = String.valueOf(map.get("orderId"));
        String result = null;
        System.out.println(orderID);
        URL url = new URL("https://pay.shop.10086.cn/paygw/WXafterpayeBack?orderId=" + orderID + "&_=1630471023757");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Host", "shop.10086.cn");
        connection.setRequestProperty("Origin", "https://shop.10086.cn");
        connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("Sec-Fetch-Dest", "empty");
        connection.setRequestProperty("Sec-Fetch-Mode", "cors");
        connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        connection.setRequestProperty("Cookie", bean.getCookies());
        //https://sdc2.10086.cn/dcsk3kicu1wpwoi05d4e1wp39_2x2s/dcs.gif?WT.branch=mall&dcssip=pay.shop.10086.cn&WT.host=pay.shop.10086.cn&dcsuri=%2Fpaygw%2F523125007078886736-1630398608192-8b3baabd73a9b2c358f099922704c16a-20.html&WT.es=https%3A%2F%2Fpay.shop.10086.cn%2Fpaygw%2F523125007078886736-1630398608192-8b3baabd73a9b2c358f099922704c16a-20.html&dcsref=https%3A%2F%2Fpay.shop.10086.cn%2Fpaygw%2F523125007078886736-1630398608192-8b3baabd73a9b2c358f099922704c16a-20.html&WT.referrer=https%3A%2F%2Fpay.shop.10086.cn%2Fpaygw%2F523125007078886736-1630398608192-8b3baabd73a9b2c358f099922704c16a-20.html&WT.sr=1920x1080&WT.ti=%E4%B8%AD%E5%9B%BD%E7%A7%BB%E5%8A%A8-%E7%A7%BB%E5%8A%A8%E5%95%86%E5%9F%8E-%E6%94%AF%E4%BB%98&WT.cg_n=Pay&WT.tx_i=523125007078886736&WT.co_f=221982e8dfc3ba59bd21630332959552&dcsdat=1630399516809&WT.category=523125007078886700_20&WT.event=CheckInfo_ConfirmPay&DCS.dcsuri=%2Fnopv.gif
        InputStream inputStream = connection.getInputStream();
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        result = new String(bytes, "UTF-8");
        System.out.println(result);
    }


    public void doBuyAjax() throws IOException {
        String result = null;
        URL url = new URL(bean.getAjaxurl());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Host", "shop.10086.cn");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
        connection.setRequestProperty("Cookie", "JSESSIONID=617798470B2BAEDBF7DEA81D8ED6ADD7; collect_id=nbp0rqs16r9y3s71j0vchj5pyymc8wt7; cart_code_key=gd51htoof5en75skqc9bdfota0; 2rIsMFMtwcdSO=5F9uANpBUNl87DBo.2IKSVuer4hM02LUNX_mzMHESn6c_F66Mz.11.zIKdo3.4RBKtU8powbM0RHwg2otZgUIuq; defaultloginuser_p=izr73fwOUuimT7R+YElqbvQdIEKrmWCpu49KY4pe7cglQnOlbxDN0nqcpR0yt5wiGBlrjQ+8vL4Y1VXDvn0uLyMTsRVyT13VInOal6sQlEY+dvBVErR/ksPv5W6XILGzNIChi3gihwmhVzzoGOae/QnMeBYb75bpd0tqC/3nGNmNwSyqpJkEqg/LuT1QHsyO; CaptchaCode=EESVMS; _gscu_1502255179=30389523wlqvry59; _gscbrs_1502255179=1; CmLocation=200|200; key4IE89=8DD6DC617660F48EB4D49C45A8E659975F3533B412612EA3332BDBD624F84B8205AF10959234DE642256DACAAAB8545E; CmProvid=bj; jsessionid-cmcc=n951749EC437142F95C4520E26A382637-1; cart_number_key=dvug4mfojmuo7pvsqcqg7bvbt2; footprint_key=dvug4mfojmuo7pvsqcqg7bvbt2; webUid_webUid=549dab5d-f7fc-76d2-9867-cdd0dee39870; lgToken=4cc129880e694cd6bbe115baca958201; sendflag=20210901131149816386; cmccssotoken=d4b4111d21bb4dbea0885fddfd21b916@.10086.cn; is_login=true; c=d4b4111d21bb4dbea0885fddfd21b916; mobile=58499-22114-5115-28940; WT_FPC=id=221982e8dfc3ba59bd21630332959552:lv=1630474686483:ss=1630469054269; 2rIsMFMtwcdSP=53n0spCWzl43cqGJyvwbuxqo4CBWGd5u1uexzvR0JNbAZT9KOnVm17E93gmJa8a0M2Uu2Qrpn3hJy_oemDeSewvZjuPmg8Sc.wr4qtiqRb0sf9GEYwn0wbUooLouqPtQPSCOGYbnLNYUIXkBHUgFD8XZtLaTLqvoN7clrjA.O.5qxzIQvgGKGL7IuvRxpDugVcl1yGBRBzeIcmu176fcLjIUJDnOScgDxgGXVlIqUWBW9GHtF7rrdlZULTv52OAjWzg.OPN9oO7BgOslzjW6v6ue8BbLgUOCNfCEi9WRRikxlbBEp5KhSQnkO1Hmm90ZcFkImZbUTEt1fr1vBkOW.bz");
        connection.setRequestProperty("Referer", "https://pay.shop.10086.cn/paygw/523312816095163582-1630586417476-28079f6eaf58f953165b08ab88677f20-20.html");

        InputStream inputStream = connection.getInputStream();
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        result = new String(bytes,"UTF-8");
        System.out.println(result);
    }
    //data:bankAbbr=WXPAY&orderId=523200222011486690&type=C&ipAddress=113.68.138.60&ts=1630473823705&hmac=5faf8c37473c501e8523772591dfa79d&channelId=20
}
