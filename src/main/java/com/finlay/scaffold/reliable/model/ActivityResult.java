package com.finlay.scaffold.reliable.model;

import lombok.Data;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-23 3:07 下午
 */
@Data
public class ActivityResult {
    //活动唯一id: redis存储
    private String activityId;
    //0 失败，1成功
    private Integer status;
}
