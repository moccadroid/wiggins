package org.lalelu.wiggins.requests.json;

import org.lalelu.wiggins.selectors.json.JsonSelector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class JsonRequest<T> {
    private Class<T> klass = null;

    private Stack<String> currentPath = new Stack<String>();
    private Map<String, String> pathMap = new HashMap<String, String>();

    private ArrayList<T> objectList = new ArrayList<T>();

    private ArrayList<JsonObjectModel> objectModels = new ArrayList<JsonObjectModel>();
    private JsonObjectModel mainObjectModel = null;
    private Map<String, JsonObjectModel> objectModelMap = new HashMap<String, JsonObjectModel>();

    private boolean isCompiled = false;

    public JsonRequest(Class<T> klass) {
        this.klass = klass;
    }

    public void compile() {
        pathMap.clear();
        for(JsonObjectModel objectModel : objectModels) {
            for (JsonSelector selector : objectModel.getSelectors()) {
                pathMap.put(selector.getSelectorPath(), selector.getField());
                objectModelMap.put(selector.getSelectorPath(), objectModel);
            }
        }
        isCompiled = true;
    }

    public boolean isCompiled() {
        return isCompiled;
    }

    public void addObjectModel(JsonObjectModel objectModel) {
        if(objectModel.getKlass().equals(this.klass)) {
            mainObjectModel = objectModel;
        }
        objectModels.add(objectModel);
        isCompiled = false;
    }

    public ArrayList<T> getResult(Map<String, Object> map) {
        if(mainObjectModel == null)
            return objectList;

        if(!isCompiled())
            compile();

        try {

            Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                currentPath.push(entry.getKey());
                parseResult(entry.getValue());
                currentPath.pop();
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return objectList;
    }

    private String getCurrentPath() {
        String curPath = "";
        Iterator iter = currentPath.iterator();
        while(iter.hasNext()) {
            curPath += iter.next() + "/";
        }
        curPath = curPath.substring(0, curPath.length() - 1);
        return curPath;
    }

    private Object parseResult(Object object) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if(object instanceof String) {
            String path = getCurrentPath();
            String field = pathMap.get(path);

            JsonObjectModel objectModel = objectModelMap.get(path);
            if(objectModel != null) {
                objectModel.assembleObject(String.class, field, object);
            }
        } else if(object instanceof Integer) {
            String path = getCurrentPath();
            String field = pathMap.get(path);

            JsonObjectModel objectModel = objectModelMap.get(path);
            if(objectModel != null) {
                objectModel.assembleObject(Integer.class, field, object);
            }
        } else if(object instanceof Double) {
            String path = getCurrentPath();
            String field = pathMap.get(path);

            JsonObjectModel objectModel = objectModelMap.get(path);
            if(objectModel != null) {
                objectModel.assembleObject(Double.class, field, object);
            }
        } else if(object instanceof Boolean) {
            String path = getCurrentPath();
            String field = pathMap.get(path);

            JsonObjectModel objectModel = objectModelMap.get(path);
            if(objectModel != null) {
                objectModel.assembleObject(Boolean.class, field, object);
            }
        } else if(object instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> arrayList = (List<Object>) object;
            for(Object arrayObject : arrayList) {
                parseResult(arrayObject);
            }
        } else if(object instanceof Map) {
            @SuppressWarnings("unchecked")
            Iterator<Map.Entry<String, Object>> iter = ((Map)object).entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                if(entry.getValue() != null) {
                    for(JsonObjectModel objectModel : objectModelMap.values()) {
                        if(objectModel.getCurrentObject() == null)
                            objectModel.createObject();

                        if(objectModel.getObjectIndex().equals(0)) {
                            if(objectModel.isInObject()) {
                                if(!objectModel.equals(mainObjectModel)) {
                                    Method method = mainObjectModel.getKlass().getMethod(objectModel.getField(), objectModel.getKlass());
                                    method.invoke(mainObjectModel.getCurrentObject(), objectModel.getCurrentObject());
                                } else {
                                    objectList.add(klass.cast(objectModel.getCurrentObject()));
                                }
                                objectModel.createObject();
                            }
                        }
                    }
                    currentPath.push(entry.getKey());
                    parseResult(entry.getValue());
                    currentPath.pop();
                }
            }
        }

        return object;
    }
}
