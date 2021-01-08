package teste;

import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SellerDao sellerDao = DaoFactory.cretaeSellerDao();
		
		System.out.println(sellerDao.findById(3));
	}

}
