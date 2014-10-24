package org.lalelu.wiggins.requests.xml;

import org.lalelu.wiggins.errors.ExceptionPool;
import org.lalelu.wiggins.selectors.xml.XmlSelector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class XmlRequest<T> {
    private Class<T> klass = null;
    private XmlObjectModel mainObjectModel = null;
    private Stack<String> currentPath = new Stack<String>();


    private Map<String, String> pathMap = new HashMap<String, String>();
    private Map<String, List<XmlObjectModel>> objectModelMap = new HashMap<String, List<XmlObjectModel>>();
    private List<XmlObjectModel> objectModels = new ArrayList<XmlObjectModel>();

    private boolean isCompiled = false;

    private List<T> objectList = new ArrayList<T>();

    public XmlRequest(Class<T> klass) {
        this.klass = klass;
    }

    public void addObjectModel(XmlObjectModel objectModel) {
        if(objectModel.getKlass().equals(this.klass)) {
            mainObjectModel = objectModel;
        }
        objectModels.add(objectModel);
        isCompiled = false;
    }

    public void compile() {
        pathMap.clear();
        for(XmlObjectModel objectModel : objectModels) {
            for (XmlSelector selector : objectModel.getSelectors()) {
                pathMap.put(selector.getSelectorPath(), selector.getObjectField());
                System.out.println("compile: "+ selector.getSelectorPath());
                if(objectModelMap.containsKey(selector.getSelectorPath())) {
                    objectModelMap.get(selector.getSelectorPath()).add(objectModel);
                } else {
                    List<XmlObjectModel> list = new ArrayList<XmlObjectModel>();
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

    public List<T> getResult(String xml) {
        objectList.clear();

        if(!isCompiled)
            compile();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Long before = System.currentTimeMillis();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            Long after = System.currentTimeMillis();
            System.out.println("parsed in: " + (after - before) + "ms");

            before = System.currentTimeMillis();
            currentPath.push(document.getDocumentElement().getTagName());
            parse(document.getDocumentElement());
            after = System.currentTimeMillis();
            System.out.println("traversed in: " + (after - before) + "ms");

        } catch (Exception e) {
            ExceptionPool.getInstance().addException(e);
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

    private void parse(Node node) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if(node instanceof Element) {
            Element element = (Element) node;

            String path = getCurrentPath();
            List<XmlObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for(XmlObjectModel objectModel : list) {
                    objectModel.assembleObject(path, element);
                }
            }

            NodeList nodeList = node.getChildNodes();
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                if(currentNode instanceof Element) {
                    for(XmlObjectModel objectModel : objectModels) {
                        if(objectModel.getCurrentObject() == null)
                            objectModel.createObject();

                        if(objectModel.getObjectIndex().equals(0)) {
                            if(objectModel.isInObject() || !objectModel.hasSelectors()) {
                                System.out.println(objectModel.getKlass().getSimpleName() + " " + objectModel.isComplete());

                                if(!objectModel.equals(mainObjectModel)){
                                    XmlObjectModel parent = objectModel.getParent();
                                    parent.increaseChildrenIndex();
                                    Method method = parent.getKlass().getMethod(objectModel.getParentField(), objectModel.getKlass());
                                    method.invoke(parent.getCurrentObject(), objectModel.getCurrentObject());
                                }
                                objectModel.createObject();
                            }
                        }
                    }
                    if(mainObjectModel.isComplete()) {
                        objectList.add(klass.cast(mainObjectModel.getCurrentObject()));
                        mainObjectModel.createObject();
                    }

                    currentPath.push(((Element)currentNode).getTagName());
                    parse(currentNode);
                    currentPath.pop();
                }
            }
        }
    }
}
