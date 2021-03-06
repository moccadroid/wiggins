package org.lalelu.wiggins.requests;

import org.lalelu.wiggins.conditions.BreakCondition;
import org.lalelu.wiggins.errors.ExceptionPool;
import org.lalelu.wiggins.selectors.Selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Request<T> {
    protected Class<T> klass = null;

    protected ObjectModel mainObjectModel = null;

    protected Map<String, String> pathMap = new HashMap<String, String>();
    protected Map<String, List<ObjectModel>> objectModelMap = new HashMap<String, List<ObjectModel>>();
    protected List<ObjectModel> objectModels = new ArrayList<ObjectModel>();

    protected boolean isCompiled = false;

    protected List<T> objectList = new ArrayList<T>();

    public Request(Class<T> klass) {
        this.klass = klass;
        ExceptionPool.getInstance().register(this);
    }

    public void addObjectModel(ObjectModel objectModel) {
        if(objectModel.getKlass().equals(this.klass)) {
            mainObjectModel = objectModel;
        }
        objectModels.add(objectModel);
        isCompiled = false;
    }

    public void compile() {
        pathMap.clear();
        for(ObjectModel objectModel : objectModels) {
            for (Selector selector : objectModel.getSelectors()) {
                pathMap.put(selector.getSelectorPath(), selector.getObjectField());
                if(objectModelMap.containsKey(selector.getSelectorPath())) {
                    objectModelMap.get(selector.getSelectorPath()).add(objectModel);
                } else {
                    List<ObjectModel> list = new ArrayList<ObjectModel>();
                    list.add(objectModel);
                    objectModelMap.put(selector.getSelectorPath(), list);
                }
            }
        }
        isCompiled = true;
    }

    protected boolean testBreakConditions(ObjectModel objectModel, Object object, String field) {
        if(objectModel.hasBreakConditions()) {
            List<BreakCondition> breakConditions = objectModel.getBreakConditions().get(field);
            if (breakConditions != null) {

                for(BreakCondition condition : breakConditions) {
                    if(condition.test(object)) {
                        objectModel.setBreakConditionFound(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCompiled() {
        return isCompiled;
    }

}
