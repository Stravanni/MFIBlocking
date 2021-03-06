package il.ac.technion.ie.bitsets;

import il.ac.technion.ie.model.BitSetFactory;
import il.ac.technion.ie.model.BitSetIF;

public class SingleBSFactory implements BitSetFactory{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1437137442141269554L;
	private static SingleBSFactory self = null;
	
	private SingleBSFactory(){}
	
	public static SingleBSFactory getInstance(){
		if(self == null){
			self = new SingleBSFactory();
		}
		return self;
	}

	@Override
	public BitSetIF createInstance() {
		return new SingleBitBS();
	}

}
