package org.lalelu.wiggins.requests.csv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lalelu.wiggins.WigginsCentral;
import org.lalelu.wiggins.conditions.BreakCondition;
import org.lalelu.wiggins.data.csvparser.CsvParser;
import org.lalelu.wiggins.errors.ExceptionHandler;
import org.lalelu.wiggins.errors.ExceptionPool;
import org.lalelu.wiggins.requests.ObjectModel;
import org.lalelu.wiggins.requests.Request;

public class CsvRequest<T> extends Request<T> {

    private Map<Integer, List<CsvObjectModel>> headerMap = new HashMap<Integer, List<CsvObjectModel>>();
    private boolean isNoHeader = false;

    public CsvRequest(Class<T> klass) {
        super(klass);
    }

    public void setNoHeader(boolean isNoHeader) {
        this.isNoHeader = isNoHeader;
    }

    public List<T> getResult(String csvString) {

        List<String[]> rowList;
        CsvParser csvParser = WigginsCentral.getCsvParser();
        String[] header;

        try {
            rowList = csvParser.parseCsv(csvString);
            if(rowList.size() > 0) {
                header = rowList.get(0);
                createHeaderMap(header);

                // if we have a header then remove the first line
                if(isNoHeader)
                    rowList.remove(0);

            } else {
                return objectList;
            }

            mainObjectModel.createObject();
            for(String[] row : rowList) {
                for(int i = 0; i < row.length; i++) {
                    boolean conditionBreak = false;

                    List<CsvObjectModel> list = headerMap.get(i);
                    if(list != null) {
                        for (CsvObjectModel objectModel : list) {

                            if (objectModel != null) {

                                conditionBreak = testBreakConditions(objectModel, row[i], ""+i);
                                if(conditionBreak) {
                                    break;
                                }

                                try {

                                    if(objectModel.getCurrentObject() == null)
                                        objectModel.createObject();

                                    if(isNoHeader)
                                        objectModel.assembleObject(""+i, row[i]);
                                    else
                                        objectModel.assembleObject(header[i], row[i]);

                                    if(objectModel.getObjectIndex().equals(0)) {
                                        if (!objectModel.equals(mainObjectModel)) {
                                            ObjectModel parent = objectModel.getParent();
                                            parent.increaseChildrenIndex();
                                            Method method = parent.getKlass().getMethod(objectModel.getParentField(), objectModel.getKlass());
                                            method.invoke(parent.getCurrentObject(), objectModel.getCurrentObject());
                                            objectModel.createObject();
                                        }
                                    }
                                } catch(NumberFormatException e) {
                                    ExceptionPool.getInstance().addException(e, this);
                                } catch (NoSuchMethodException e) {
                                    ExceptionPool.getInstance().addException(e, this);
                                } catch (InstantiationException e) {
                                    ExceptionPool.getInstance().addException(e, this);
                                } catch (IllegalAccessException e) {
                                    ExceptionPool.getInstance().addException(e, this);
                                } catch (InvocationTargetException e) {
                                    ExceptionPool.getInstance().addException(e, this);
                                } catch (Exception e) {
                                    ExceptionPool.getInstance().addException(e, this);
                                }
                            }
                        }
                        if(mainObjectModel.isComplete()) {
                            objectList.add(klass.cast(mainObjectModel.getCurrentObject()));
                            mainObjectModel.createObject();
                        }
                    }
                    if(conditionBreak) {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            ExceptionPool.getInstance().addException(e, this);
        }
        return objectList;
    }

    private void createHeaderMap(String[] header) {
        for(int i = 0; i < header.length; i++) {
            String field = header[i];

            if(isNoHeader)
                field = "" + i;

            for(ObjectModel objectModel : objectModels) {
                CsvObjectModel csvObjectModel = (CsvObjectModel) objectModel;
                if(csvObjectModel.containsObjectField(field)) {
                    if(headerMap.containsKey(i)) {
                        headerMap.get(i).add(csvObjectModel);
                    } else {
                        List<CsvObjectModel> list = new ArrayList<CsvObjectModel>();
                        list.add(csvObjectModel);
                        headerMap.put(i, list);
                    }
                }
            }
        }
    }
}
