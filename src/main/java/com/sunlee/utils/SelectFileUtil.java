package com.sunlee.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.junit.Test;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 将用户搜索的内容进行解析
 *
 * @author sunlee
 * @date 2020/2/26 11:08
 */
public class SelectFileUtil {


    public static List<String> strFromToList(String strSelect) {

        //对 html 标签进行转义，防止 XSS 攻击
        strSelect = HtmlUtils.htmlEscape(strSelect);
        //去除特殊符号
        strSelect = strSelect.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", "");
        //进行结巴分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> lists = segmenter.sentenceProcess(strSelect);

        //为每个单词添加 %%
        List<String> fromLists = new ArrayList<>();
        for (String list :lists) {
            fromLists.add("%"+list+"%");
        }

        while (fromLists.size() < 5) {
            fromLists.add("1=1");
        }

        for (String list :fromLists) {
            System.out.println(list);
        }
        return fromLists;
    }

    @Test
    public void fun() {
        System.out.println(SelectFileUtil.strFromToList("我的工作总结"));
    }
}
