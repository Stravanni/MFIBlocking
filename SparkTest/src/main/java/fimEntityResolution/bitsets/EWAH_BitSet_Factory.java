package fimEntityResolution.bitsets;

import java.io.Serializable;

import fimEntityResolution.interfaces.BitSetFactory;
import fimEntityResolution.interfaces.BitSetIF;

public class EWAH_BitSet_Factory implements BitSetFactory,Serializable{

	private static EWAH_BitSet_Factory self = null;

	private EWAH_BitSet_Factory() {
	}

	public static EWAH_BitSet_Factory getInstance() {
		if (self == null) {
			self = new EWAH_BitSet_Factory();
		}
		return self;
	}

	
	public BitSetIF createInstance() {
		return new EWAH_BitSet();
	}
}
