package org.lalelu.wiggins.requests.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.lalelu.wiggins.WigginsCentral;
import org.lalelu.wiggins.data.jsonparser.JsonParser;
import org.lalelu.wiggins.selectors.json.JsonSelector;

public class JsonRequest<T> {
    private Class<T> klass = null;

    private Stack<String> currentPath = new Stack<String>();
    private Map<String, String> pathMap = new HashMap<String, String>();

    private ArrayList<T> objectList = new ArrayList<T>();

    private ArrayList<JsonObjectModel> objectModels = new ArrayList<JsonObjectModel>();
    private JsonObjectModel mainObjectModel = null;
    private Map<String, List<JsonObjectModel>> objectModelMap = new HashMap<String, List<JsonObjectModel>>();

    private boolean isCompiled = false;

    public JsonRequest(Class<T> klass) {
        this.klass = klass;
    }

    public void compile() {
        pathMap.clear();
        for(JsonObjectModel objectModel : objectModels) {
            for (JsonSelector selector : objectModel.getSelectors()) {
                pathMap.put(selector.getSelectorPath(), selector.getObjectField());
                if(objectModelMap.containsKey(selector.getSelectorPath())) {
                    objectModelMap.get(selector.getSelectorPath()).add(objectModel);
                } else {
                    List<JsonObjectModel> list = new ArrayList<JsonObjectModel>();
                    list.add(objectModel);
                    objectModelMap.put(selector.getSelectorPath(), list);
                }
            }
        }
        isCompiled = true;
    }

    public boolean isCompiled() {
        return isCompiled;
    }

    public JsonObjectModel getJsonObjectModel(Class<?> klass) {
        for(JsonObjectModel objectModel : objectModels) {
            if(objectModel.getKlass().equals(klass))
                return objectModel;
        }
        // TODO: can we do something about this null value?
        return null;
    }

    public void addObjectModel(JsonObjectModel objectModel) {
        if(objectModel.getKlass().equals(this.klass)) {
            mainObjectModel = objectModel;
        }
        objectModels.add(objectModel);
        isCompiled = false;
    }

    public ArrayList<T> getResult(String json) {
        JsonParser jsonParser = WigginsCentral.getJsonParser();
        Map<String, Object> map = jsonParser.parseJson(json);

        return getResult(map);
    }

    public ArrayList<T> getResult(Map<String, Object> map) {
        if(mainObjectModel == null) {
            return objectList;
        }

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objectList;
    }

    private String getCurrentPath() {
        if(currentPath.isEmpty())
            return "";

        String curPath = "";
        Iterator<String> iter = currentPath.iterator();
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

            List<JsonObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for (JsonObjectModel objectModel : objectModelMap.get(path)) {
                    objectModel.assembleObject(String.class, path, object);
                }
            }
        } else if(object instanceof Integer) {
            String path = getCurrentPath();
            String field = pathMap.get(path);

            List<JsonObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for (JsonObjectModel objectModel : objectModelMap.get(path)) {
                    objectModel.assembleObject(Integer.class, path, object);
                }
            }
        } else if(object instanceof Double) {
            String path = getCurrentPath();
            String field = pathMap.get(path);

            List<JsonObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for (JsonObjectModel objectModel : objectModelMap.get(path)) {
                    objectModel.assembleObject(Double.class, path, object);
                }
            }
        } else if(object instanceof Boolean) {
            String path = getCurrentPath();
            String field = pathMap.get(path);

            List<JsonObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for (JsonObjectModel objectModel : objectModelMap.get(path)) {
                    objectModel.assembleObject(Boolean.class, path, object);
                }
            }
        } else if(object instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> arrayList = (List<Object>) object;
            for(Object arrayObject : arrayList) {
                parseResult(arrayObject);
            }
        } else if(object instanceof Map) {
            @SuppressWarnings("unchecked")
            Iterator<Map.Entry<String, Object>> iter = ((Map<String,Object>)object).entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                if(entry.getValue() != null) {
                    for(List<JsonObjectModel> list : objectModelMap.values()) {
                        for(JsonObjectModel objectModel : list) {
                            if (objectModel.getCurrentObject() == null)
                                objectModel.createObject();

                            if (objectModel.getObjectIndex().equals(0)) {
                                if (objectModel.isInObject()) {
                                    if (!objectModel.equals(mainObjectModel)) {
                                        JsonObjectModel parent = objectModel.getParent();
                                        Method method = parent.getKlass().getMethod(objectModel.getParentField(), objectModel.getKlass());
                                        method.invoke(parent.getCurrentObject(), objectModel.getCurrentObject());
                                    } else {
                                        objectList.add(klass.cast(objectModel.getCurrentObject()));
                                    }
                                    objectModel.createObject();
                                }
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
