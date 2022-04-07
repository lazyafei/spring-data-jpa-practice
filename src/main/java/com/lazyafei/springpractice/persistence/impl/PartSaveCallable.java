package com.lazyafei.springpractice.persistence.impl;

import com.lazyafei.springpractice.conf.EntityManagerHelper;
import com.lazyafei.springpractice.model.callable.CallableResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;


@Slf4j
public class PartSaveCallable<S> implements Callable {

    private List<S> lists;
    private CountDownLatch cdl;
    private Boolean isSave;

    public PartSaveCallable(List<S> lists, CountDownLatch cdl,Boolean isSave){
        this.lists = lists;
        this.cdl = cdl;
        this.isSave = isSave;
    }

    @Override
    @Transactional
    public CallableResultVo call() {
        CallableResultVo<S> result = new CallableResultVo<>();
        List<S> resultList = new ArrayList<>();
        String su = isSave?"插入":"更新";
        log.info("--------------线程"+Thread.currentThread()+"开始执行" + su + "操作!，当前cdl is -------：" + cdl.getCount() + " -----------------------------");

        //当前线程em
        EntityManager em = EntityManagerHelper.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();


        try {
            log.info("-------------current EntityManager is ：{} -------------",em);
            EntityManagerHelper.beginTransaction();
            //log.info("-------------list Size is ：{} -------------",lists.size());
            for(S s : lists){
                if(isSave){
                    em.persist(s);
                }else{
                    em.merge(s);
                }
                resultList.add(s);
            }
            EntityManagerHelper.commitTransaction();
            EntityManagerHelper.closeEntityManager();
            result.setResult(resultList);
            log.info("-------------线程"+Thread.currentThread()+su+"完成，当前cdl(--) is -------：" + cdl.getCount() + " -------------");
            cdl.countDown();
        }catch (RuntimeException e){
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }

            log.error("发生错误:{}, 异常信息:{}", e);
        }
        return result;
    }
}