package com.zqz.dao.mapper;


import com.zqz.dao.entity.DbMovies;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbMoviesMapper {
    int insert(DbMovies record);

    DbMovies selectByMovieId(String movieId);

    List<DbMovies> getAll();

    List<DbMovies> selectByParam(@Param("title") String title, @Param("casts") String casts);
}