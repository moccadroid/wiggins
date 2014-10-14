package org.lalelu.wiggins.requests.csv;

import org.lalelu.wiggins.WigginsCentral;
import org.lalelu.wiggins.data.csvparser.CsvParser;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvRequest<T> {

    private Class<T> klass = null;
    private List<T> objectList = new ArrayList<T>();
    private boolean isCompiled = false;

    private Map<Integer, List<CsvObjectModel>> headerMap = new HashMap<Integer, List<CsvObjectModel>>();

    private CsvObjectModel mainObjectModel = null;
    private List<CsvObjectModel> objectModels = new ArrayList<CsvObjectModel>();

    public CsvRequest(Class klass) {
        this.klass = klass;
    }

    public void compile() {

        isCompiled = true;
    }

    public void addObjectModel(CsvObjectModel objectModel) {
        if(objectModel.getKlass().equals(klass)) {
            mainObjectModel = objectModel;
        }

        objectModels.add(objectModel);
        isCompiled = false;
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
                rowList.remove(0);
            } else {
                return objectList;
            }

            mainObjectModel.createObject();
            for(String[] row : rowList) {
                for(int i = 0; i < row.length; i++) {

                    List<CsvObjectModel> list = headerMap.get(i);
                    if(list != null) {
                        for (CsvObjectModel objectModel : list) {

                            if (objectModel != null) {
                                objectModel.createObject();

                                objectModel.assembleObject(header[i], row[i]);
                                if (objectModel.getObjectIndex().equals(0)) {
                                    if (objectModel.equals(mainObjectModel)) {
                                        objectList.add(klass.cast(mainObjectModel.getCurrentObject()));
                                    } else {
                                        Method method = mainObjectModel.getKlass().getMethod(objectModel.getField(), objectModel.getKlass());
                                        method.invoke(mainObjectModel.getCurrentObject(), objectModel.getCurrentObject());
                                    }
                                }
                            }
                        }
                    }

                }
            }

        } catch (Exception e) {
            //LOG.error("getResult", e);
        }
        return objectList;
    }

    private void createHeaderMap(String[] header) {
        for(int i = 0; i < header.length; i++) {
            for(CsvObjectModel objectModel : objectModels) {
                if(objectModel.containsObjectField(header[i])) {
                    if(headerMap.containsKey(i)) {
                        headerMap.get(i).add(objectModel);
                    } else {
                        List<CsvObjectModel> list = new ArrayList<CsvObjectModel>();
                        list.add(objectModel);
                        headerMap.put(i, list);
                    }
                }
            }
        }
    }
}
