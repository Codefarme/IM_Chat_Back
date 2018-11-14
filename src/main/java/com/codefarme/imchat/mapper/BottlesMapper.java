package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.Bottles;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BottlesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bottles record);

    int insertSelective(Bottles record);

    Bottles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Bottles record);

    int updateByPrimaryKey(Bottles record);

    List<Integer> getMyBottles(String account);

    Integer getBottlesNum(List<Integer> list);

    Integer getBottlesNum0(List<Integer> list);

    Bottles getOneBottle(@Param("list") List<Integer> list, @Param("integer") Integer integer);

    Bottles getOneBottle0(@Param("list") List<Integer> list, @Param("integer") Integer integer);

    void deleteBottle(Integer id);
}