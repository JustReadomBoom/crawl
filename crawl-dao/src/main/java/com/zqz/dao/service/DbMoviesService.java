package com.zqz.dao.service;

import com.zqz.dao.entity.DbMovies;
import com.zqz.dao.mapper.DbMoviesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 14:32 2021/2/20
 */
@Service
public class DbMoviesService {
    @Resource
    private DbMoviesMapper mapper;


    public int insert(DbMovies movies){
        return mapper.insert(movies);
    }

    public DbMovies selectByMovieId(String movieId){
        return mapper.selectByMovieId(movieId);
    }

    public List<DbMovies> getAll() {
        return mapper.getAll();
    }

    public List<DbMovies> selectByParam(String title, String casts){
        return mapper.selectByParam(title, casts);
    }

}
