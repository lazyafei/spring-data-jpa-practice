package com.lazyafei.springpractice.persistence.impl;

import com.lazyafei.springpractice.model.callable.CallableResultVo;
import com.lazyafei.springpractice.persistence.BatchSaveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * @Author lazyafei
 * @Date 1/12/21 10:51 PM
 */
@NoRepositoryBean
@Slf4j
public class BatchSaveRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BatchSaveRepository<T> {

    //每个线程分的数据量
    private final Integer BATCH_SIZE = 1500;
    //最大线程数（建议最大电脑核心数*2）
    private final Integer MAX_THREAD = 4;

    private static EntityManager em = null;

    public BatchSaveRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
    }


    //@Async
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <S extends T> List<S> batchSave(Iterable<S> entities,Boolean isSave){
        //返回list数组，需要带id
        List<S> result = new ArrayList<>();

        List<S> lists = new ArrayList<>();
        List<S> listsTmp;
        entities.forEach(lists::add);
        Integer listSize = lists.size();

        //构造线程池 - 默认最大MaxThread个线程同时执行，每个线程执行数据量BATCH_SIZE
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD);
        //需要循环测次数，最后一次大概率不满足一个BATCH_SIZE
        Integer loopCount = listSize/BATCH_SIZE+1;
        //倒计时门闩 - await() 让线程等待，用countDown()消初始化数量。当数量等于0时线程唤醒
        CountDownLatch cdl = new CountDownLatch(loopCount);//使用计数器
        //创建FutureList,存储每一个线程返回的结果
        List<Future> futureSaveList = new ArrayList<>();
        List<Future> futureUpdateList = new ArrayList<>();

        //一共循环threadNum次
        for(int i = 0; i < loopCount; i++){
            if(i == loopCount-1){
                //走到头但不足一整次的部分
                log.info("----------------------拆分数据最后一部分下标范围：start - {}, end - {}.",i*BATCH_SIZE,listSize);
                listsTmp = lists.subList(i*BATCH_SIZE,listSize);
                PartSaveCallable<S> psc =  new PartSaveCallable<>(listsTmp,cdl,isSave);
                if(isSave){
                    futureSaveList.add(executorService.submit(psc));
                }else{
                    futureUpdateList.add(executorService.submit(psc));
                }

            }else{
                log.info("----------------------拆分数据下标范围：start - {}, end - {}.",i*BATCH_SIZE,(i+1)*BATCH_SIZE);
                listsTmp = lists.subList(i*BATCH_SIZE,(i+1)*BATCH_SIZE);
                PartSaveCallable<S> psc =  new PartSaveCallable<>(listsTmp,cdl,isSave);
                if(isSave){
                    futureSaveList.add(executorService.submit(psc));
                }else{
                    futureUpdateList.add(executorService.submit(psc));
                }
            }
        }


        try {
            //确保线程执行完
            cdl.await();

            List<Future> tempFutureList = isSave?futureSaveList:futureUpdateList;
            for(Future future : tempFutureList){
                //线程到这儿必定执行完了
                try {
                    Object res = future.get();
                    if(res != null){
                        CallableResultVo<S> crv = (CallableResultVo<S>) res;
                        result.addAll(crv.getResult());
                    }
                } catch (ExecutionException e) {
                    log.info("子线程写回数据时发生异常 :{} ",e);
                }
            }
        } catch (InterruptedException e) {
            log.info("子线程发生中断异常 :{} ",e);
        }finally {
            //执行完关闭线程池
            executorService.shutdown();
        }

        return result;
    }





}





