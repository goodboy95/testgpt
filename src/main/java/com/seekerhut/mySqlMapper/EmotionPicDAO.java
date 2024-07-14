package com.seekerhut.mySqlMapper;

import com.seekerhut.model.mysqlModel.User;
import com.seekerhut.mySqlMapper.MyBatisBaseDao;
import com.seekerhut.model.mysqlModel.EmotionPic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EmotionPicDAO继承基类
 */
@Repository
public interface EmotionPicDAO extends MyBatisBaseDao<EmotionPic, Integer> {
    public List<EmotionPic> getAll();
}