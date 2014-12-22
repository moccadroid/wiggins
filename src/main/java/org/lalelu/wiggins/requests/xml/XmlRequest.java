package org.lalelu.wiggins.requests.xml;

import org.lalelu.wiggins.conditions.BreakCondition;
import org.lalelu.wiggins.errors.ExceptionPool;
import org.lalelu.wiggins.requests.ObjectModel;
import org.lalelu.wiggins.requests.Request;
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

public class XmlRequest<T> extends Request<T> {
    private Stack<String> currentPath = new Stack<String>();
    //private Map<String, List<ObjectModel>> objectModelMap = new HashMap<String, List<ObjectModel>>();
    //private List<ObjectModel> objectModels = new ArrayList<ObjectModel>();

    public XmlRequest(Class<T> klass) {
        super(klass);
    }

    public List<T> getResult(String xml) {
        objectList.clear();
        currentPath.clear();

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

            List<ObjectModel> list = objectModelMap.get(path);
            if(list != null) {
                for(ObjectModel objectModel : list) {

                    if(!testBreakConditions(objectModel, element, path))
                        ((XmlObjectModel)objectModel).assembleObject(path, element);
                }
            }

            testBreakConditions(mainObjectModel, element, path);

            NodeList nodeList = node.getChildNodes();
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                if(currentNode instanceof Element) {
                    for(ObjectModel objectModel : objectModels) {
                        if(objectModel.getCurrentObject() == null)
                            objectModel.createObject();

                        if(objectModel.getObjectIndex().equals(0)) {
                            if(objectModel.isInObject() || !objectModel.hasSelectors()) {

                                if(!objectModel.equals(mainObjectModel)){
                                    ObjectModel parent = objectModel.getParent();
                                    parent.increaseChildrenIndex();
                                    Method method = parent.getKlass().getMethod(objectModel.getParentField(), objectModel.getKlass());
                                    method.invoke(parent.getCurrentObject(), objectModel.getCurrentObject());
                                    objectModel.createObject();
                                }
                            }
                        }
                    }

                    if(mainObjectModel.isComplete()) {
                        if(!mainObjectModel.breakConditionFound())
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
