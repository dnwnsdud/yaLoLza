package com.web.project.function;

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
public class SummonerTimeList {
          private Long purchasetime;
          private Long purchaseitem;
          
}
