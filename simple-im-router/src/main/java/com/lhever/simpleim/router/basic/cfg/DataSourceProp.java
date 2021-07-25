package com.lhever.simpleim.router.basic.cfg;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataSourceProp {
    private String username;
    private String password;
    private String url;
    private String driver;
}
