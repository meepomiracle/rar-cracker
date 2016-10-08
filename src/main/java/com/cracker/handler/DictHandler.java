package com.cracker.handler;

import com.cracker.domain.CrackerParam;

/**
 * Created by hadoop on 2016/9/25.
 */
public class DictHandler {

    private CrackerParam crackerParam;

    public DictHandler(CrackerParam crackerParam) {
        this.crackerParam = crackerParam;
    }

    public CrackerParam getCrackerParam() {
        return crackerParam;
    }

    public void setCrackerParam(CrackerParam crackerParam) {
        this.crackerParam = crackerParam;
    }

    public void doProcess(){

    }
}
