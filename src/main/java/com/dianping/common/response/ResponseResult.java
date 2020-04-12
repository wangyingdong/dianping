package com.dianping.common.response;


import lombok.*;

@Builder
@Data
@Setter
@Getter
public class ResponseResult<T> {
    // 接口调用成功或者失败
    @Builder.Default
    private Integer code = 200;
    // 需要传递的信息，例如错误信息
    @Builder.Default
    private String status = "success";
    // 需要传递的数据
    private T data;

}
