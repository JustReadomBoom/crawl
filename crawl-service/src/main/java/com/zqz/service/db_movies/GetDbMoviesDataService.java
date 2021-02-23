package com.zqz.service.db_movies;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zqz.dao.entity.DbMovies;
import com.zqz.dao.service.DbMoviesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 14:38 2021/2/20
 */
@Service
@Slf4j
public class GetDbMoviesDataService {
    @Autowired
    private DbMoviesService dbMoviesService;

    private static final String BASE_ADDRESS = "https://Movie.douban.com/j/new_search_subjects?sort=U&range=0,10&tags=&start=";


    public void getData() {
        int start;
        int end = 9979;
        for (start = 9770; start <= end; start += 209) {
            try {
                Thread.sleep(2000);
                String address = BASE_ADDRESS + start;
                JSONObject dataLine = doGetData(address, 1);
                if (null != dataLine) {
                    System.out.println(dataLine.getString("msg"));
                    JSONArray data = dataLine.getJSONArray("data");
                    if (null != data) {
                        System.out.println("data : " + data);
                        saveData(data);
                    } else {
                        System.out.println("未请求到数据");
                        System.out.println("start = " + start);
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("获取豆瓣数据异常:[{}]", e.getMessage(), e);
            }
        }
    }

    private void saveData(JSONArray jsonArray) {
        int index = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jOb = jsonArray.getJSONObject(i);
            String id = jOb.getString("id");
            String title = jOb.getString("title");
            String cover = jOb.getString("cover");
            BigDecimal rate = jOb.getBigDecimal("rate");
            JSONArray casts = jOb.getJSONArray("casts");
            JSONArray directors = jOb.getJSONArray("directors");

            DbMovies dbMovies = dbMoviesService.selectByMovieId(id);
            if (null == dbMovies) {
                DbMovies movies = new DbMovies();
                movies.setMovieId(id);
                movies.setDirectors(directors.toJSONString());
                movies.setTitle(title);
                movies.setCover(cover);
                movies.setRate(rate);
                movies.setCasts(casts.toJSONString());
                dbMoviesService.insert(movies);
                index = index + 1;
            } else {
                log.info("=====> MovieId:[{}] record have exist", id);
            }
        }
        System.out.println("本次成功新增 " + index + " 条记录！");
    }


    private JSONObject doGetData(String url, int comeFrom) throws Exception {
        URL realUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("Host", "movie.douban.com");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("Cookie", "bid=KYd19kZsBkY; douban-fav-remind=1; ap_v=0,6.0; __utmc=30149280; __gads=ID=ba9dd8d82e1cee58-22b4652321c60041:T=1614042995:RT=1614042995:S=ALNI_MYb1U-uV-Ydg1yx2w5K797wXKbeMg; ll=\"118281\"; __utma=30149280.1664162308.1614042994.1614042994.1614046363.2; __utmz=30149280.1614046363.2.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmt=1; __utmb=30149280.2.9.1614046363; __utma=223695111.1588163397.1614046378.1614046378.1614046378.1; __utmb=223695111.0.10.1614046378; __utmc=223695111; __utmz=223695111.1614046378.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _pk_ses.100001.4cf6=*; __yadk_uid=hcqFUwEZC5DRjqOXWix4eGrsjbNPEETt; _vwo_uuid_v2=D40F1551A8B5A809A8A1842A9FE82E031|716f2f00cea5ca71d264a90f28627eae; dbcl2=\"196482108:IT2j87/PNnE\"; ck=eqj_; _pk_id.100001.4cf6=aac1e72d58f034d8.1614046378.1.1614046411.1614046378.; push_noty_num=0; push_doumail_num=0");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36");
        connection.connect();
        if (connection.getResponseCode() == 200) {
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[10485760];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            String jsonString = baos.toString();
            baos.close();
            is.close();
            return getJsonString(jsonString, comeFrom);
        }
        return null;
    }

    private JSONObject getJsonString(String str, int comeFrom) {
        if (comeFrom == 1) {
            return JSON.parseObject(str);
        } else if (comeFrom == 2) {
            int indexStart = 0;
            //字符处理
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '(') {
                    indexStart = i;
                    break;
                }
            }
            String strNew = "";
            //分割字符串
            for (int i = indexStart + 1; i < str.length() - 1; i++) {
                strNew += str.charAt(i);
            }
            return JSON.parseObject(strNew);
        }
        return null;
    }

}
