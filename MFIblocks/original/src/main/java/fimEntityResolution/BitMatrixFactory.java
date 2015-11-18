package fimEntityResolution;

import il.ac.technion.ie.context.MfiContext;
import il.ac.technion.ie.data.structure.BitMatrix;
import il.ac.technion.ie.data.structure.Clearer;
import il.ac.technion.ie.utils.ClearerFactory;

public class BitMatrixFactory implements ClearerFactory{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static BitMatrixFactory self = null;
	private BitMatrixFactory(){}
	
	public static BitMatrixFactory getInstance(){
		if(self == null){
			self = new BitMatrixFactory();
		}
		return self;
	}
	
	
	@Override
	public Clearer createInstance() {
        Clearer clearer = new BitMatrix(MfiContext.getInstance().getDBSize());
        return clearer;
    }

	
}
