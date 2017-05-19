package tech.caols.infinitely;

import java.util.ArrayList;
import java.util.List;

public class CallBacks implements CallBack {

    private List<CallBack> list = new ArrayList<>();

    public CallBacks add(CallBack callBack) {
        this.list.add(callBack);
        return this;
    }

    @Override
    public void call() {
        for (CallBack callBack: this.list) {
            callBack.call();
        }
    }

}
