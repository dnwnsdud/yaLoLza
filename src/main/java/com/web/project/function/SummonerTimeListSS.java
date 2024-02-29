package com.web.project.function;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.Data;


@Data
@Component
@Builder
@Lazy
@Scope("prototype")
public class SummonerTimeListSS {
    List<SummonerTimeList> summonerTimeList;
}
