package com.seekerhut.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.bean.LoginReturn;
import com.seekerhut.model.mysqlModel.EmotionPic;
import com.seekerhut.model.mysqlModel.User;
import com.seekerhut.mySqlMapper.EmotionPicDAO;
import com.seekerhut.mySqlMapper.UserDAO;
import com.seekerhut.utils.JedisHelper;
import com.seekerhut.utils.MailUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service("EmotionListService")
public class EmotionListService {
    private DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyyMM");
    private EmotionPicDAO emotionPicMapper;

    public EmotionListService(EmotionPicDAO emotionPicMapper) {
        this.emotionPicMapper = emotionPicMapper;
    }

    public int addEmotionPic(String emotionToken, String emotionPicPath) {
        var emotionPic = new EmotionPic() {
            {
                setEmotionPath(emotionPicPath);
                setEmotionToken(emotionToken);
            }
        };
        var effectLines = emotionPicMapper.insertSelective(emotionPic);
        return effectLines > 0 ? 0 : -1;
    }

    public List<EmotionPic> getEmotionPicList() {
        var emotionPicList = emotionPicMapper.getAll();
        return emotionPicList;
    }
}


