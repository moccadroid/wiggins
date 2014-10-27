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
import org.lalelu.wiggins.requests.ObjectModel;
import org.lalelu.wiggins.requests.Request;
import org.lalelu.wiggins.selectors.Selector;
import org.lalelu.wiggins.selectors.json.JsonSelector;

public class JsonRequest<T> extends Request<T> {
    private Stack<String> currentPath = new Stack<String>();

    public JsonRequest(Class<T> klass) {
        super(klass);
    }

    public List<T> getResult(String json) {
        JsonParser jsonParser = WigginsCentral.getJsonParser();
        Map<String, Object> map = jsonParser.parseJson(json);

        return getResult(map);
    }

    public List<T> getResult(Map<String, Object> map) {
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

            List<ObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for (ObjectModel objectModel : objectModelMap.get(path)) {
                    ((JsonObjectModel)objectModel).assembleObject(String.class, path, object);
                }
            }
        } else if(object instanceof Integer) {
            String path = getCurrentPath();

            List<ObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for (ObjectModel objectModel : objectModelMap.get(path)) {
                    ((JsonObjectModel)objectModel).assembleObject(Integer.class, path, object);
                }
            }
        } else if(object instanceof Double) {
            String path = getCurrentPath();

            List<ObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for (ObjectModel objectModel : objectModelMap.get(path)) {
                    ((JsonObjectModel)objectModel).assembleObject(Double.class, path, object);
                }
            }
        } else if(object instanceof Boolean) {
            String path = getCurrentPath();

            List<ObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for (ObjectModel objectModel : objectModelMap.get(path)) {
                    ((JsonObjectModel)objectModel).assembleObject(Boolean.class, path, object);
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
                    for(List<ObjectModel> list : objectModelMap.values()) {
                        for(ObjectModel objectModel : list) {
                            if (objectModel.getCurrentObject() == null)
                                objectModel.createObject();

                            if (objectModel.getObjectIndex().equals(0)) {
                                if (objectModel.isInObject()) {
                                    if (!objectModel.equals(mainObjectModel)) {
                                        ObjectModel parent = objectModel.getParent();
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
