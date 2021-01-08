package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
	
		PreparedStatement st = null;
		ResultSet 		  rs = null;		
		
		try {
			
			String sql;
			
			sql = " insert into seller (name,email,birthdate,basesalary,departmentid) ";
			sql = sql + " values (?,?,?,?,?) ";
			
			st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1,obj.getName());
			st.setString(2,obj.getEmai());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4,obj.getBaseSalary() );
			st.setInt(5, obj.getDepartamento().getId());
			
			int rowAffected = st.executeUpdate();
			
			if (rowAffected>0) {
				
				rs = st.getGeneratedKeys();
				if (rs.next()){
					int id = rs.getInt(1);
					obj.setId(id);				
				}				
			}
			else {
			
				throw new DbException("Erro Inesperado");
				
			}				
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);			
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		
		try {
			
			String sql;
			
			sql = " update seller set name = ? ,email = ?,birthdate = ?,basesalary = ?,departmentid=? ";
			sql = sql + " where id=? ";
			
			st = conn.prepareStatement(sql);
			
			st.setString(1,obj.getName());
			st.setString(2,obj.getEmai());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4,obj.getBaseSalary() );
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());			
			st.executeUpdate();			
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			
			String sql;
			
			sql = " delete from seller where id=? ";
			
			st = conn.prepareStatement(sql);
			
			st.setInt(1,id);			
			st.executeUpdate();			
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(st);
		}	
	}

	@Override
	public Seller findById(Integer id) {
		// TODO Auto-generated method stub
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select seller.*,department.name as DepName" 
					+" from seller ,department where  seller.departmentid=department.id and"
					+" seller.id=?");
			
			st.setInt(1, id);
			rs=st.executeQuery();
			if (rs.next()) {
				Department dep = instatiateDepartment(rs);
				Seller obj     = instatiateSeller(rs,dep);
				return obj;
			}
			else {
				return null;
			}
					
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instatiateSeller(ResultSet rs,Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmai(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartamento(dep);
		return obj;
	}

	private Department instatiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select seller.*,department.name as DepName" 
					+" from seller ,department where  seller.departmentid=department.id ");

			rs=st.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer,Department> map = new HashMap();
			
			while (rs.next()) {
				
				Department depto = map.get(rs.getInt("DepartmentId"));
			
				if (depto == null) {
					depto = instatiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), depto);
				}
				
				Seller obj     = instatiateSeller(rs,depto);
				list.add(obj);
			}
			return list;					
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}	}

	@Override
	public List<Seller> FindByDepartment(Department dep) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select seller.*,department.name as DepName" 
					+" from seller ,department where  seller.departmentid=department.id and"
					+" department.id=? order by department.name ");
			
			st.setInt(1,dep.getId());
			rs=st.executeQuery();
			Department depto;
			List<Seller> list = new ArrayList<>();
			Map<Integer,Department> map = new HashMap();
			while (rs.next()) {
				depto = instatiateDepartment(rs);
				Seller obj     = instatiateSeller(rs,depto);
				list.add(obj);
			}
			return list;					
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
