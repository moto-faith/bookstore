package com.xupeng.edu.service.impl;

import com.xupeng.edu.exception.UserException;
import com.xupeng.edu.model.Category;
import com.xupeng.edu.model.User;
import com.xupeng.edu.service.ICategoryService;
import com.xupeng.edu.service.IUserService;
import com.xupeng.edu.service.base.BaseServiceImpl;
import com.xupeng.edu.utils.JedisUtil;
import com.xupeng.edu.utils.SendJMail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:31
 */
@Service
@Transactional
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements ICategoryService {


    @Override
    public List<Category> findAll() {

        Jedis jedis = JedisUtil.getJedis();
//        Set<String> categorys = jedis.zrange("category", 0, -1);
        Set<Tuple> categorys = jedis.zrangeWithScores("bookCategory", 0, -1);
        List<Category> cs=  null;
        if (categorys==null||categorys.size()==0){
//            System.out.println("from mysql");
            cs = categoryMapper.findAll();
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("bookCategory",cs.get(i).getCid(),cs.get(i).getCname());
            }

        }else {
//            System.out.println("from redis");
            cs = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                cs.add(category);
            }
        }
        return cs;


    }
}
