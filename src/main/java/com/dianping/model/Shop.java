package com.dianping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shop")
public class Shop  implements Serializable {

    private static final long serialVersionUID = 833061313510667766L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "name")
    @NotBlank(message = "名称不能为空")
    private String name;

    @Column(name = "remark_score")
    private BigDecimal remarkScore;

    @Column(name = "price_per_man")
    private Integer pricePerMan;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Transient
    private Integer distance;

    @Column(name = "category_id")
    private Integer categoryId;

    @Transient
    private Category categoryModel;

    @Column(name = "tags")
    private String tags;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "address")
    private String address;

    @Column(name = "seller_id")
    private Integer sellerId;

    @Transient
    private Seller sellerModel;

    @Column(name = "icon_url")
    private String iconUrl;


}