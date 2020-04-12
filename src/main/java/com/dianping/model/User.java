package com.dianping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User  implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    @Column(name = "updated_ad")
    private LocalDateTime updateAd;

    @Column(name = "telphone")
    @NotBlank(message = "手机号码不能为空")
    private String telphone;

    @NotBlank(message = "密码不能为空")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 0
     */
    @NotNull(message = "性别不能为空")
    @Column(name = "gender")
    private Integer gender;
}