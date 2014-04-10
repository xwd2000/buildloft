package com.example.buildloft.consts;

public class AppConst {
	public static final short CATEGORYBIT_CRANE=1;
	public static final short CATEGORYBIT_BOUND=2;
	public static final short CATEGORYBIT_LINK=4;
	public static final short CATEGORYBIT_OBJ=8;
	
	public static final short MASK_CRANE=CATEGORYBIT_CRANE+CATEGORYBIT_BOUND+CATEGORYBIT_LINK+CATEGORYBIT_OBJ;
	public static final short MASK_BOUND=CATEGORYBIT_CRANE+CATEGORYBIT_BOUND+CATEGORYBIT_LINK+CATEGORYBIT_OBJ;
	public static final short MASK_LINK=CATEGORYBIT_LINK+CATEGORYBIT_BOUND+CATEGORYBIT_CRANE;
	public static final short MASK_OBJ=CATEGORYBIT_CRANE+CATEGORYBIT_BOUND+CATEGORYBIT_OBJ;
}
