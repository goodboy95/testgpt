package com.seekerhut.mySqlMapper;

import com.seekerhut.model.mysqlModel.Dialog;
import org.springframework.stereotype.Repository;

/**
 * DialogDAO继承基类
 */
@Repository
public interface DialogDAO extends MyBatisBaseDao<Dialog, Long> {
}