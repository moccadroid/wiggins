package org.lalelu.brivel.brivelplus.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.lalelu.brivel.brivelplus.BrivelCentral;
import org.lalelu.brivel.brivelplus.database.provider.DatabaseAccessProvider;
import org.lalelu.brivel.brivelplus.requests.Request;
import org.lalelu.brivel.brivelplus.selectors.Selector;

public class DatabaseFacade {
    private DatabaseAccessProvider databaseAccessProvider = null;

    public DatabaseFacade() {
        this.databaseAccessProvider = BrivelCentral.getDatabaseAccessProvider();
    }

    public <T> Request<T> updateRequest(Request<T> request) {
        if(!request.isUpdateCompiled()) {
            request.compileUpdate();
        }

        databaseAccessProvider.execute(request.getUpdateSql());

        return request;
    }

    public <T> Request<T> mergeRequest(Request<T> request) {
        if(!request.isInsertCompiled()) {
            request.compileInsert();
        }

        databaseAccessProvider.execute(request.getInsertSql());

        return request;
    }

    public <T> Request<T> getResult(Request<T> request) {
        try {
            if (!request.isSelectCompiled())
                request.compileSelect();

            String sql = request.getSelectSql();
            List<Object[]> result = databaseAccessProvider.getResultList(sql);

            ReuseableDeepDataObjectAssembler assembler = new ReuseableDeepDataObjectAssembler();
            for (Object[] row : result) {
            	assembler.selectorList = request.getSelectSelectors();
            	assembler.values = row;
            	request.assembleAndAddObject(assembler);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }
    
    public interface DataObjectAssembler {
		public <E> E assembleObject(Request<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException;
	}
	
	protected class ReuseableDeepDataObjectAssembler implements DataObjectAssembler {
		protected List<Selector<?>> selectorList;
		protected Object[] values;
		protected ReuseableDeepDataObjectAssembler dof = null; // init lazy or we run in trouble ...
		
		public <E> E assembleObject(Request<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
			Class<E> klass = request.getKlass();
			E object = klass.newInstance();
			
			int cnt;
            for(cnt = 0; cnt < selectorList.size(); cnt++) {
                Selector<?> selector = selectorList.get(cnt);
                Method method = klass.getMethod("set" + selector.getFieldName(), selector.getType());
                method.invoke(object, selector.getDataConverter().read(values[cnt]));
            }

           
            Map<String, Request<?>> subRequests = request.getSubRequests();
            if(!subRequests.isEmpty() && dof == null) dof = new ReuseableDeepDataObjectAssembler();
            for(Map.Entry<String, Request<?>> entry : subRequests.entrySet()) {
            	Request<?> subRequest = entry.getValue();
            	dof.selectorList = subRequest.getSelectSelectors();
            	dof.values = Arrays.copyOfRange(values, cnt, dof.selectorList.size());
            	cnt += dof.selectorList.size();
            	
            	Object subObject = subRequest.assembleAndAddObject(dof);
                
                Method method = klass.getMethod("set" + entry.getKey(), subRequest.getKlass());
         		method.invoke(object, subObject);
            }
            request.addObject(object);
            return object;
		}
	}
}
