package thisisdumb;

public class CoreMod {

	public int EM;
	public int EXP;
	public int KIN;
	public int THERM;

	CoreMod(int EM, int EXP, int KIN, int THERM)
	{
		this.EM = EM;
		this.EXP = EXP;
		this.KIN = KIN;
		this.THERM = THERM;
	}
	
	public boolean isValid()
	{
		return this.EM + this.EXP + this.KIN + this.THERM < 50;
	}
}
