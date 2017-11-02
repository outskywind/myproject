package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class JDBCTest {
	
	@Test
	public void testMysql(){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//jdbc 连接参数
			DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","qcy123");
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOracle(){
		
		String url="jdbc:oracle:thin:@192.168.137.128:1521:oracle";
		String user="scott";
		String passwd="tiger";
		
		try {
			//1.加载jdbc驱动
			Class.forName("oracle.jdbc.OracleDriver").newInstance();
			//2.获取连接
			Long start=System.currentTimeMillis();
			
			Connection conn=DriverManager.getConnection(url,user,passwd);
			
			Long end = System.currentTimeMillis();
			System.out.println("get connection cost"+(end-start)+"ms");
			
			//3.
			conn.setAutoCommit(false);
			
			Statement stmt =  conn.createStatement();
			
			String sql = "select * from scott.emp";
			
			stmt.executeQuery(sql);
			
			String sql2 = "insert into scott.bonus values('SMITH','CLERK',800,null)";
			
			stmt.executeUpdate(sql2);
			
			//没提交到数据库，但是stmt还是会返回结果
			//原因
			//有一个人执行了update的sql语句，但还没把该connection提交， 
			//如果是同一个connection的话，另一个人statement执行select的会看到这个update的结果 
			//如果是两个不同的connection的话，另一个人执行的select的就还只是update执行之前的数据
			
			ResultSet rs =stmt.executeQuery("select * from scott.bonus");
			if(rs.next()){ 
				String name = rs.getString("ename");
				String job = rs.getString("job");
				int sal = rs.getInt("sal");
				int comm = rs.getInt("comm");
				System.out.println("before conn.commit ,the result is :"+name+" "+job+" "+sal+" "+comm);
			} else{
				System.out.println("before conn.commit, result  not commited");
			}
			
			conn.commit();
			
			rs =stmt.executeQuery("select * from scott.bonus");
			if(rs.next()){
				String name = rs.getString("ename");
				String job = rs.getString("job");
				int sal = rs.getInt("sal");
				int comm = rs.getInt("comm");
				System.out.println("after conn.commit ,the result is :"+name+" "+job+" "+sal+" "+comm);
			}
			
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Connection getConnection(String type){
		Connection connection=null;
		if("DataSource".equals(type)){
			//OracleDataSourceFactory factory = new OracleDataSourceFactory();
			//OralceDataSource ds = factory.getObjectInstance(obj, name, context, hashtable);
			
		}
		else{
			
		}
		
		return connection;
	}
	

}
