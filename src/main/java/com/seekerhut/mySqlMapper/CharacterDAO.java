package com.seekerhut.mySqlMapper;

import com.seekerhut.model.mysqlModel.Character;
import org.springframework.stereotype.Repository;

/**
 * CharacterDAO继承基类
 */
@Repository
public interface CharacterDAO extends MyBatisBaseDao<Character, Integer> {
}