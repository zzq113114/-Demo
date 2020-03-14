package test;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-test.xml");
		SessionFactory sf = (SessionFactory) ctx.getBean("mySessionFactory");
		Session ss = null;
		Transaction ta = null;
		try {
			ss = sf.openSession();
			ta = ss.beginTransaction();
			Query q = ss.createQuery("from RES_DataElement a order by a.DataElementId");
			List<Map<String, Object>> result = q.list();
			int index = 0;
			for(Map<String, Object> d:result){
				index++;
				Integer deId = (Integer) d.get("DataElementId");
				q = ss.createQuery("update RES_DataSetContent a set a.DataElementId = ? where a.DataElementId = ?");
				q.setInteger(0, index);
				q.setInteger(1, deId);
				q.executeUpdate();
				q = ss.createQuery("update RES_DataElement a set a.DataElementId = ? where a.DataElementId = ?");
				q.setInteger(0, index);
				q.setInteger(1, deId);
				q.executeUpdate();
			}
			ta.commit();
		} catch (Exception e) {
			ta.rollback();
			e.printStackTrace();
		} finally {
			ss.close();
		}
	}

}
