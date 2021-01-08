package teste;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SellerDao sellerDao = DaoFactory.cretaeSellerDao();
		
		System.out.println(sellerDao.findById(3));
		//List<Seller> list = sellerDao.FindByDepartment(new Department(2,null));
		List<Seller> list = sellerDao.findAll();
		for (Seller obj:list) {
			System.out.println(obj);
			System.out.println(obj.getDepartamento().hashCode());
		}
		
		/*Seller newSeller = new Seller(null,"Gilbero","gilbertouba@gmail.com",new Date(),4000.00,new Department(2,null));
		
		sellerDao.insert(newSeller);*/
		
		Seller sellerupd = sellerDao.findById(8);
		if (sellerupd!=null) {
			sellerupd.setName("Gilberto de Oliveira");
			sellerDao.update(sellerupd);
			System.out.println("alterado");			
		}
		sellerDao.deleteById(9);
		
	}

}
