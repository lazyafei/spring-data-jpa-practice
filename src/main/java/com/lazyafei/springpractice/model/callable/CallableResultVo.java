package com.lazyafei.springpractice.model.callable;

import lombok.Data;

import java.util.List;

@Data
public class CallableResultVo<T> {
    List<T> result;
}
