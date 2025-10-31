package com.peng.PengAirline.enums;

import lombok.Getter;

@Getter//通过一个 getter 方法 来访问私有字段private final Country country;
public enum City {

    //Nigeria
    LAGOS(Country.NIGERIA),
    ABUJA(Country.NIGERIA),

    //USA
    MIAMI(Country.USA),
    DALLAS(Country.USA),

    //UK
    LONDON(Country.UK),
    LEEDS(Country.UK);

    private final Country country;//private + getter 能防止外部直接修改字段。

    City(Country country){
        this.country = country;
    }

}
