package dao;

import dao.blog_news.DAOBlognews;
import dao.marketplace.DAOMarketplace;
import dao.twitter.DAOTwitter;

public class InitialContext {
	public Object lookup(String serviceName){
        if(serviceName.equalsIgnoreCase("DAOBlognews")){
            System.out.println("Looking up and creating a new DAOBlognews object");
            return new DAOBlognews();
        }else if (serviceName.equalsIgnoreCase("DAOMarketplace")){
            System.out.println("Looking up and creating a new DAOMarketplace object");
            return new DAOMarketplace();
        } else if (serviceName.equalsIgnoreCase("DAOTwitter")){
            System.out.println("Looking up and creating a new DAOTwitter object");
            return new DAOTwitter();
        } 
        return null;
    }
}
